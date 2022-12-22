package com.gals.prayertimes.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AthanSettingsViewModelFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AthanSettingsViewModel::class.java)) {
            return AthanSettingsViewModel(
                repository = repository,
                context = context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}