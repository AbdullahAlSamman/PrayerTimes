package com.gals.prayertimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.services.NotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.getTodayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashscreenViewModel @Inject constructor(
    private val repository: Repository,
    private val tools: UtilsManager
) : ViewModel() {
    val loading = MutableLiveData<Boolean>()

    fun prepareApp() {
        viewModelScope.launch {
            loading.postValue(true)
            repository.refreshSettings()
            if (repository.refreshPrayer(getTodayDate())) {
                launchNotificationService(repository.getSettings()!!)
                loading.value = false
            }
        }
    }

    private fun launchNotificationService(settingsEntity: SettingsEntity) {
        if (!tools.isServiceRunning(NotificationService::class.java)) {
            if (settingsEntity.notification) {
                tools.startService(NotificationService::class.java)
            }
        } else {
            if (!settingsEntity.notification) {
                tools.stopService(NotificationService::class.java)
            }
        }
    }
}