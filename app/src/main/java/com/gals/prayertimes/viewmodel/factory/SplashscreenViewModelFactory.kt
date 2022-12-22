package com.gals.prayertimes.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.viewmodel.SplashscreenViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SplashscreenViewModelFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) {
            return SplashscreenViewModel(
                context = context,
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}