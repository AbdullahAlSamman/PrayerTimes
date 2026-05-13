package com.gals.prayertimes.ads

import android.content.Context
import androidx.compose.ui.unit.Dp
import com.gals.prayertimes.ads.model.AdPlacement
import com.google.android.gms.ads.AdView

interface AdsManager {
    fun initAdsSDK()
    fun requestBannerAd(context: Context, maxAdHeight: Dp, adPlacement: AdPlacement): AdView
    fun loadAd(adview: AdView)
}