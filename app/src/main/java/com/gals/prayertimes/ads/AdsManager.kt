package com.gals.prayertimes.ads

import android.content.Context
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

interface AdsManager {
    fun initAdsSDK()
    fun requestAdView(context: Context, adSize: AdSize): AdView
    fun requestAdViewWithEvents(
        context: Context,
        adSize: AdSize,
        onAdLoaded: () -> Unit,
        onAdOpened: () -> Unit,
        onAdClicked: () -> Unit,
        onAdImpression: () -> Unit,
        onAdFailedToLoad: (error: String) -> Unit
    ): AdView

    fun loadAd(adview: AdView)
}