package com.gals.prayertimes.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.services.NotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashscreenViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {
    private val tools: UtilsManager = UtilsManager(context)
    val loading = MutableLiveData<Boolean>()
    var savedSettings: Settings = Settings.EMPTY

    fun prepareApp() {
        viewModelScope.launch {
            loading.postValue(true)
            var response: Boolean
            withContext(Dispatchers.IO) {
                refreshSettings()
                savedSettings = getSettings()
                response = getPrayer()
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
            if (savedSettings.notification) {
                context.startService(Intent(context, NotificationService::class.java))
            }
        } else {
            if (!savedSettings.notification) {
                context.stopService(Intent(context, NotificationService::class.java))
            }
        }
    }

    private fun refreshSettings() = repository.refreshSettings()

    private fun getSettings() = repository.getSettingsFromLocalDataSource()!!

    private suspend fun getPrayer(): Boolean = repository.refreshPrayer(getTodayDate())
}