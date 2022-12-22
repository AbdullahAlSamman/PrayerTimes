package com.gals.prayertimes.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.MainViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val tools: UtilsManager,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val calculation: PrayerCalculation
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                tools = tools,
                repository = repository,
                resourceProvider = resourceProvider,
                calculation = calculation
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}