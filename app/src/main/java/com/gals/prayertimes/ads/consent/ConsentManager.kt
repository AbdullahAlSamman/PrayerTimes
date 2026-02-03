package com.gals.prayertimes.ads.consent

import android.app.Activity
import com.google.android.ump.FormError

interface ConsentManager {
    val canRequestAds: Boolean
    val isPrivacyOptionsRequired: Boolean

    suspend fun gatherConsent(
        activity: Activity
    ): FormError?

    fun showPrivacyOptionsForm(
        activity: Activity,
        onDismiss: (FormError?) -> Unit
    )

    fun resetConsent(){}
}