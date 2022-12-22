package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.SplashscreenViewModel
import javax.inject.Inject

class SplashscreenViewModelFactory @Inject constructor(
    private val repository: Repository,
    private val tools : UtilsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) {
            return SplashscreenViewModel(
                repository = repository,
                tools = tools
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}