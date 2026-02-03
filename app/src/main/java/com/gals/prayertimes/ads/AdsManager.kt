package com.gals.prayertimes.ads

import com.gals.prayertimes.ads.consent.ConsentManager
import javax.inject.Inject

interface AdsManager {
    fun initAdsSDK()
}


class AdsManagerImpl @Inject constructor(
    consentManager: ConsentManager
) : AdsManager{
    override fun initAdsSDK() {
        //TODO check consent canRequestAds then call init
    }
}