package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.viewmodel.SplashscreenViewModel

class SplashscreenViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) {
            return SplashscreenViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}