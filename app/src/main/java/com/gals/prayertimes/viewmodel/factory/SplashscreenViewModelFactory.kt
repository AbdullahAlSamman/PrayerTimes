package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.utils.Repository
import com.gals.prayertimes.viewmodel.SplashscreenViewModel

class SplashscreenViewModelFactory(
    private val database: AppDB,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashscreenViewModel::class.java)) {
            return SplashscreenViewModel(database, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}