package com.gals.prayertimes.viewmodel

import androidx.lifecycle.ViewModel
import com.gals.prayertimes.R
import com.gals.prayertimes.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    resourceProvider: ResourceProvider
) : ViewModel() {
    val url: String = resourceProvider.getString(R.string.asset_url_privacy_policy)
}