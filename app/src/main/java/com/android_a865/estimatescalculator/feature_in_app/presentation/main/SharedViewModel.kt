package com.android_a865.estimatescalculator.feature_in_app.presentation.main

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.android_a865.estimatescalculator.feature_in_app.domain.model.AppProducts
import com.android_a865.estimatescalculator.feature_in_app.domain.model.Security
import com.android_a865.estimatescalculator.feature_settings.domain.models.AppSettings
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import com.android_a865.estimatescalculator.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val tag = "SharedViewModel"


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: SettingsRepository,
    @ApplicationContext context: Context
) : ViewModel() {


    private var appSetting: AppSettings? = null

    val isSubscribed = MutableLiveData(false)

    val products = MutableStateFlow(AppProducts())

    private val purchaseListener: PurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                handlePurchases(purchases)
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                getPurchases()
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                showMessage("Purchase Canceled")
            } else {
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
                isSubscribed.value = it.isSubscribed
            }
        }
        Log.d(tag, "initiated")
    }

    fun subscribe(productId: String) = billingClient.doOnReady {
        initiatePurchase(productId)
    }

    private fun initiatePurchase(productId: String) {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(arrayListOf(productId)).setType(BillingClient.SkuType.SUBS)


        val billingResult =
            billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

            billingClient.querySkuDetailsAsync(params.build()) { billingResult0, skuDetailsList ->
                if (billingResult0.responseCode == BillingClient.BillingResponseCode.OK) {

                    if (skuDetailsList != null && skuDetailsList.size > 0) {


                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList.first())
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

    private fun showMessage(msg: String) = viewModelScope.launch {
        eventsChannel.send(WindowEvents.ShowMessage(msg))
    }


    private fun queryProductsDetails() {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(
            arrayListOf(
                MONTHLY_SUBSCRIPTION,
                YEARLY_SUBSCRIPTION
            )
        ).setType(BillingClient.SkuType.SUBS)


        val billingResult =
            billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

            billingClient.querySkuDetailsAsync(params.build()) { billingResult0, skuDetailsList ->
                if (billingResult0.responseCode == BillingClient.BillingResponseCode.OK) {

                    if (skuDetailsList != null && skuDetailsList.size > 0) {

                        val monthly = skuDetailsList.filter {
                            it.sku == MONTHLY_SUBSCRIPTION
                        }

                        val yearly = skuDetailsList.filter {
                            it.sku == YEARLY_SUBSCRIPTION
                        }

                        if (monthly.isNotEmpty()) {
                            products.update {
                                it.copy(
                                    monthly = monthly.first()
                                )
                            }
                        }

                        if (yearly.isNotEmpty()) {
                            products.update {
                                it.copy(
                                    yearly = yearly.first()
                                )
                            }
                        }


                    } else {
                        showMessage("Item not found")
                    }
                } else {
                    showMessage("Error0 ${billingResult0.debugMessage}")
                }
            }

        } else {
            showMessage("Sorry Subscription not supported")
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        val validProducts = listOf(MONTHLY_SUBSCRIPTION, YEARLY_SUBSCRIPTION)
        val validPurchases = mutableListOf<String>()

        for (purchase in purchases) {

            val isSubscribed = isValidPurchase(purchase)

            if (isSubscribed) {
                val productId = purchase.skus.first()
                if (productId in validProducts) {
                    validPurchases.add(productId)
                }
            }

        }

        setIsSubscribed(validPurchases.isNotEmpty())
    }

    private fun isValidPurchase(purchase: Purchase): Boolean {
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {


                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    // Invalid purchase
                    // show error to user
                    showMessage("Error : Invalid Purchase")
                    return false
                    //skip current iteration only because other items in purchase list
                    // must be checked if present
                }
                // else purchase is valid
                //if item is subscribed and not Acknowledged
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(
                        acknowledgePurchaseParams
                    ) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            //if purchase is acknowledged
                            // then saved value in preference
                            showMessage(" Item Subscribed")
                        }
                    }
                } else {
                    // Grant entitlement to the user on item purchase
                    val subscribed = isSubscribed.value ?: false
                    if (!subscribed) {
                        // save Subscribe Item Value To Pref as true)
                        showMessage(" Item Subscribed")
                    }
                }
                return true

            }
            Purchase.PurchaseState.PENDING -> {
                showMessage("Subscription is pending, Please complete Transaction")
                return false
            }
            Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                showMessage("Subscription Status Unknown")
                return false
            }
            else -> {
                return false
            }
        }
    }


    fun launchBillingFlow(activity: Activity, flowParams: BillingFlowParams) {
        billingClient.launchBillingFlow(activity, flowParams)
    }

    fun onAppStarted() {
        // to force init called from MainActivity
        getPurchases()
    }

    fun getProductsData() {
        val subscribed = isSubscribed.value ?: false
        val productsAlreadyLoaded = products.value.monthly != null &&
                products.value.yearly != null

        if (!subscribed && !productsAlreadyLoaded) {
            billingClient.doOnReady {
                queryProductsDetails()
            }
        }

    }

    private fun setIsSubscribed(b: Boolean) {
        viewModelScope.launch {
            repository.updateIsSubscribed(b)
        }
    }

    private fun getPurchases() {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { _, purchases ->
            Log.d(tag, "${purchases.size} purchases")
            handlePurchases(purchases)
        }
    }


    fun onCancelSubscriptionClicked() {
        // TODO
    }

    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: Exception) {
            Log.d(tag, e.message.toString())
            false
        }
    }


    sealed class WindowEvents {
        data class ShowMessage(val msg: String) : WindowEvents()
        data class LaunchBillingFlow(val flowParams: BillingFlowParams) : WindowEvents()
    }
}