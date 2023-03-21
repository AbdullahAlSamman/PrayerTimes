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
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SplashscreenViewModel @Inject constructor(
    private val repository: Repository,
    private val tools: UtilsManager
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    var savedSettingsEntity: SettingsEntity = SettingsEntity.EMPTY

    fun prepareApp() {
        viewModelScope.launch {
            loading.postValue(true)
            var response: Boolean
            withContext(Dispatchers.IO) {
                repository.refreshSettings()
                savedSettingsEntity = repository.getSettings()!!
                response = repository.refreshPrayer(getTodayDate())
            }
            withContext(Dispatchers.Main) {
                if (response) {
                    launchNotificationService()
                    loading.value = false
                }
            }
        }
    }

    private fun launchNotificationService() {
        if (!tools.isServiceRunning(NotificationService::class.java)) {
            if (savedSettingsEntity.notification) {
                tools.startService(NotificationService::class.java)
            }
        } else {
            if (!savedSettingsEntity.notification) {
                tools.stopService(NotificationService::class.java)
            }
        }
    }
}