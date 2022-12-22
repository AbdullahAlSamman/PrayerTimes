package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.viewmodel.PrivacyPolicyViewModel
import javax.inject.Inject

class PrivacyPolicyViewModelFactory @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrivacyPolicyViewModel::class.java)) {
            return PrivacyPolicyViewModel(
                resourceProvider = resourceProvider
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}