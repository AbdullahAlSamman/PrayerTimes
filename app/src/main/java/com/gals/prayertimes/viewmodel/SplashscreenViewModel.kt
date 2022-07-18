package com.gals.prayertimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashscreenViewModel(
    private val repository: Repository
) : ViewModel() {
    val loading = MutableLiveData<Boolean>()

    fun updateData() {
        viewModelScope.launch {
            loading.postValue(true)
            var response: Boolean
            withContext(Dispatchers.IO) {
                refreshSettings()
                response = getPrayer()
            }
            withContext(Dispatchers.Main) {
                if (response) {
                    loading.value = false
                }
            }
        }
    }

    private suspend fun getPrayer(): Boolean = repository.refreshPrayer(getTodayDate())

    private suspend fun refreshSettings() = repository.refreshSettings()

}