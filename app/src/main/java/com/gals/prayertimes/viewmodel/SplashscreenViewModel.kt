package com.gals.prayertimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.utils.Repository
import com.gals.prayertimes.utils.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashscreenViewModel(
    private val repository: Repository
) : ViewModel() {
    val loading = MutableLiveData<Boolean>()

    fun getPrayer() {
        viewModelScope.launch {
            loading.postValue(true)
            var response: Boolean
            withContext(Dispatchers.IO) {
                response = repository.refreshPrayer(getTodayDate())
            }
            withContext(Dispatchers.Main) {
                if (response) {
                    loading.value = false
                }
            }
        }
    }

}