package com.android_a865.estimatescalculator.feature_in_app.presentation.main

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.android_a865.estimatescalculator.feature_settings.domain.models.AppSettings
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    repository: SettingsRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    val tag = "SharedViewModel"

    private var appSetting: AppSettings? = null

    private val purchaseListener: PurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                handlePurchases(purchases)
            }
            else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {

                billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { _, listOfPurchases ->
                    handlePurchases(listOfPurchases)
                }

            }
            else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                showMessage("Purchase Canceled")
            }
            else {
                showMessage("Error")
                Log.d(tag, billingResult.debugMessage)
            }
        }

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases().setListener(purchaseListener).build()

    private val eventsChannel = Channel<WindowEvents>()
    val windowEvents = eventsChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            repository.getAppSettings().collect {
                appSetting = it
            }
        }

        Log.d(tag, "initiated")
    }

    fun subscribe(productId: String) {
        if (billingClient.isReady) {
            Log.d(tag, "billing client is ready")
            initiatePurchase(productId)
        } else {
            Log.d(tag, "getting billing client ready")
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    Log.d(tag, "billing service disconnected")
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(productId)
                    } else {
                        showMessage("Error ${billingResult.debugMessage}")
                        Log.d(tag, billingResult.debugMessage)
                    }
                }

            })
        }
    }

    private fun initiatePurchase(productId: String) {
        querySkuDetails(
            arrayListOf(
                productId
            )
        )
    }

    private fun showMessage(msg: String) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.ShowMessage(msg))
    }

    private fun querySkuDetails(skuList: ArrayList<String>) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)


        val billingResult =
            billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

            billingClient.querySkuDetailsAsync(params.build()) { billingResult0, skuDetailsList ->
                if (billingResult0.responseCode == BillingClient.BillingResponseCode.OK) {

                    Log.d(tag, "${skuDetailsList?.size}")

                    if (skuDetailsList != null && skuDetailsList.size > 0) {


                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()

                        viewModelScope.launch {
                            eventsChannel.send(WindowEvents.LaunchBillingFlow(flowParams))
                        }

                    } else {
                        showMessage("Item not found")
                    }
                } else {
                    showMessage("Error ${billingResult0.debugMessage}")
                }
            }

        } else {
            showMessage("Sorry Subscription not supported")
        }

    }

    private fun handlePurchases(purchases: List<Purchase>?) {
        purchases?.let {
            for (purchase in it) {

            }
        }

    }

    fun launchBillingFlow(activity: Activity, flowParams: BillingFlowParams) {
        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun onStart() {
        // force ini
    }


    sealed class WindowEvents {
        data class ShowMessage(val msg: String) : WindowEvents()
        data class LaunchBillingFlow(val flowParams: BillingFlowParams) : WindowEvents()
    }
}