package com.gals.prayertimes.viewmodel

import androidx.lifecycle.ViewModel
import com.gals.prayertimes.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyViewModel : ViewModel() {

    fun loadUrl(binding: FragmentPrivacyPolicyBinding) {
        binding.privacyPolicyWebView.loadUrl("file:///android_asset/privacy_policy.html")
    }
}