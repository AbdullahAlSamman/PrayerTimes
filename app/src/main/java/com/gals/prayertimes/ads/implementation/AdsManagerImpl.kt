package com.gals.prayertimes.ads.implementation

import android.content.Context
import com.gals.prayertimes.R
import com.gals.prayertimes.ads.AdPlacement
import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.utils.ResourceProvider
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
        adSize: AdSize,
        adPlacement: AdPlacement
    ): AdView {
        val adView = AdView(context)
        adView.adUnitId = adPlacement.getUnitId()
        adView.setAdSize(adSize)
        return adView
    }

    override fun requestAdViewWithEvents(
        context: Context,
        adSize: AdSize,
        adPlacement: AdPlacement,
        onAdLoaded: () -> Unit,
        onAdOpened: () -> Unit,
        onAdClicked: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToLoad: (error: String) -> Unit
    ): AdView {
        val adView = AdView(context)
        adView.adUnitId = adPlacement.getUnitId()
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
        val adRequest = AdRequest.Builder().build()
        adview.loadAd(adRequest)
    }

    private fun AdPlacement.getUnitId(): String = resourceProvider.getString(
        when (this) {
            AdPlacement.Home -> R.string.admob_home_banner_id
            AdPlacement.PrayerCalendar -> R.string.admob_prayer_calendar_banner_id
        }
    )
}