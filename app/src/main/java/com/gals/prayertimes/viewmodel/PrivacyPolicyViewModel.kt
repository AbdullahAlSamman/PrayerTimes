package com.gals.prayertimes.viewmodel

import androidx.lifecycle.ViewModel

class PrivacyPolicyViewModel : ViewModel() {
    /**TODO:get resource provider through dagger,hilt, or koin*/
    val url: String = "file:///android_asset/privacy_policy.html"
}