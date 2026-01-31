package com.gals.prayertimes.ads.manager

import android.app.Activity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AdsDebugConsentManager @Inject constructor(
    val consentInformation: ConsentInformation
) : ConsentManager {

    override val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    override val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    override suspend fun gatherConsent(
        activity: Activity
    ): FormError? {
        return suspendCancellableCoroutine { continuation ->
            requestConsent(
                activity = activity,
                onSuccess = {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }, onFailure = { formError ->
                    if (continuation.isActive) {
                        continuation.resume(formError)
                    }
                })
        }
    }

    override fun showPrivacyOptionsForm(
        activity: Activity,
        onDismiss: (FormError?) -> Unit
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(
            activity,
            onDismiss
        )
    }

    override fun resetConsent() {
        consentInformation.reset()
    }

    private fun requestConsent(
        activity: Activity,
        onSuccess: () -> Unit,
        onFailure: (FormError?) -> Unit
    ) {
        val debugSettings = ConsentDebugSettings
            .Builder(activity)
            .build()

        val params = ConsentRequestParameters.Builder()
            .setConsentDebugSettings(debugSettings)
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        onFailure(formError)
                    } else {
                        onSuccess()
                    }
                }
            },
            { requestConsentError ->
                onFailure(requestConsentError)
            }
        )
    }
}