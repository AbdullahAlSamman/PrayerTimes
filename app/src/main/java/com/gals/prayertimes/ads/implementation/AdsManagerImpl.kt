package com.gals.prayertimes.ads.implementation

import android.content.Context
import android.os.Bundle
import com.gals.prayertimes.R
import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.utils.ResourceProvider
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceProvider: ResourceProvider
) : AdsManager {

    override fun initAdsSDK() {
        MobileAds.initialize(context)
    }

    override fun requestAdView(
        context: Context,
        adSize: AdSize
    ): AdView {
        val adView = AdView(context)
        adView.adUnitId = resourceProvider.getString(R.string.admob_banner_id)
        adView.setAdSize(adSize)
        return adView
    }

    override fun requestAdViewWithListener(
        context: Context,
        adSize: AdSize,
        onAdLoaded: () -> Unit,
        onAdOpened: () -> Unit,
        onAdClicked: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToLoad: (error: String) -> Unit
    ): AdView {
        val adView = AdView(context)
        adView.adUnitId = resourceProvider.getString(R.string.admob_banner_id)
        adView.setAdSize(adSize)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                onAdLoaded()
            }

            override fun onAdOpened() {
                onAdOpened()
            }

            override fun onAdClicked() {
                onAdClicked()
            }

            override fun onAdImpression() {
                onAdImpression()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                onAdFailedToLoad(error.message)
            }
        }

        return adView
    }

    override fun loadAd(adview: AdView) {
        val extras = Bundle()
        extras.putString("collapsible", "bottom")
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        adview.loadAd(adRequest)
    }
}