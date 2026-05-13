package com.gals.prayertimes.ads.implementation

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.ads.model.AdPlacement
import com.gals.prayertimes.utils.ResourceProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
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

    override fun requestBannerAd(
        context: Context,
        maxAdHeight: Dp,
        adPlacement: AdPlacement
    ): AdView {
        val adView = AdView(context)
        adView.adUnitId = adPlacement.getUnitId()
        adView.setAdSize(maxAdHeight.getBannerAdSize())
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

    private fun Dp.getBannerAdSize(): AdSize = when {
        this >= 250.dp -> AdSize.MEDIUM_RECTANGLE
        this >= 100.dp -> AdSize.LARGE_BANNER
        else -> AdSize.BANNER
    }
}