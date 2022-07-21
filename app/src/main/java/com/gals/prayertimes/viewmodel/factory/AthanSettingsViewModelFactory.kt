package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel

class AthanSettingsViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AthanSettingsViewModel::class.java)) {
            return AthanSettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}