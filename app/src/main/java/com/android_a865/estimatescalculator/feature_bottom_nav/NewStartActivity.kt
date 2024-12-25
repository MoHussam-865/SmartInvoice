package com.android_a865.estimatescalculator.feature_bottom_nav

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.ActivityNewStartBinding
import com.android_a865.estimatescalculator.feature_in_app.presentation.main.SharedViewModel
import com.android_a865.estimatescalculator.feature_in_app.presentation.main.TAG
import com.android_a865.estimatescalculator.utils.NO_AD
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewStartBinding
    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_new_start)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_items,
                R.id.navigation_client,
                R.id.navigation_invoices,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sharedViewModel.onAppStarted()

        if (!NO_AD) {
            binding.apply {

                sharedViewModel.isSubscribed.observe(this@NewStartActivity) { isSubscribed ->
                    if (isSubscribed) {
                        Log.d(TAG, "ads must not be shown")
                        adView.isVisible = false
                    } else {

                        Log.d(TAG, "Checking Connectivity")
                        val connectivityManager =
                            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        connectivityManager.let {
                            it.registerDefaultNetworkCallback(object :
                                ConnectivityManager.NetworkCallback() {
                                override fun onAvailable(network: Network) {
                                    //take action when network connection is gained
                                    Log.d(TAG, "Connected")
                                    runOnUiThread {
                                        loadAdsOnConnected(binding)
                                    }

                                }
                            })
                        }

                    }
                }

            }
        } else binding.adView.isVisible = false

    }



    private fun loadAdsOnConnected(binding: ActivityNewStartBinding) {
        MobileAds.initialize(this@NewStartActivity)
        val adRequest = AdRequest.Builder().build()
        binding.adView.isVisible = true
        binding.adView.loadAd(adRequest)

        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        // TODO replace with ad_mob_interstitial
        InterstitialAd.load(
            this,
            getString(R.string.ad_mob_interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    sharedViewModel.myAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    sharedViewModel.myAd = interstitialAd
                    sharedViewModel.myAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                                sharedViewModel.myAd = null
                                loadInterstitialAd()
                            }
                        }

                }
            }
        )
    }


}