package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.model.UiPermissionState
import com.gals.prayertimes.model.mappers.getTimePrayerByName
import com.gals.prayertimes.model.mappers.toTimePrayer
import com.gals.prayertimes.model.mappers.todayDate
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.services.alarmmanager.AlarmItem
import com.gals.prayertimes.services.alarmmanager.AlarmManager
import com.gals.prayertimes.services.alarmmanager.AlarmWorker
import com.gals.prayertimes.services.alarmmanager.PRAYER_ALARM_WORK_NAME
import com.gals.prayertimes.services.alarmmanager.PRAYER_ALARM_WORK_NEXT_DAY_NAME
import com.gals.prayertimes.utils.PrayerCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val alarmManager: AlarmManager,
    private val prayerCalculation: PrayerCalculation,
    private val workManager: WorkManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiSelectedRadio = MutableStateFlow(NotificationType.SILENT.value)
    private val _uiSwitchState = MutableStateFlow(false)
    private val _uiSelectedPrayerAlarms =
        MutableStateFlow(PrayerName.entries.associateWith { it != PrayerName.SUNRISE })
    private val _uiPermissionState = MutableStateFlow(getPendingPermission())

    val uiSelectedRadio: StateFlow<String> = _uiSelectedRadio.asStateFlow()
    val uiSwitchState: StateFlow<Boolean> = _uiSwitchState.asStateFlow()
    val uiPermissionState: StateFlow<UiPermissionState> = _uiPermissionState.asStateFlow()
    val uiSelectedPrayerAlarms: StateFlow<Map<PrayerName, Boolean>> =
        _uiSelectedPrayerAlarms.asStateFlow()

    init {
        loadSavedSettings()
    }

    fun updateSelectedRadio(value: String) {
        _uiSelectedRadio.update { value }
    }

    fun updatePermissionState(value: UiPermissionState) {
        _uiPermissionState.update { value }
        when (value) {
            UiPermissionState.GRANTED -> {
                removePendingPermission()
            }

            UiPermissionState.DENIED -> {
                removePendingPermission()
                _uiSwitchState.update { false }
            }

            else -> {/* no-op */
            }
        }
    }

    fun updateSwitchState(value: Boolean) {
        if (value) { // Turning ON
            if (alarmManager.canScheduleAlarms()) {
                _uiSwitchState.update { true }
            } else {
                _uiPermissionState.update { UiPermissionState.REQUESTED }
            }
        } else { // Turning OFF
            _uiSwitchState.update { false }
        }
    }

    fun updateSelectedAlarms(prayerName: PrayerName, isSelected: Boolean) {
        _uiSelectedPrayerAlarms.update { oldMap -> oldMap + (prayerName to isSelected) }
    }

    fun requestAlarmPermission() {
        saveRequestedPermission()
        alarmManager.requestPermission()
    }

    fun getPendingPermissions(): UiPermissionState = getPendingPermission()

    fun isAlarmPermissionGranted(): Boolean = alarmManager.canScheduleAlarms()

    fun submitChanges() {
        val currentSettings = SettingsEntity(
            notification = _uiSwitchState.value,
            notificationType = _uiSelectedRadio.value,
            fajerNotification = _uiSelectedPrayerAlarms.value[PrayerName.FAJER] == true,
            sunriseNotification = _uiSelectedPrayerAlarms.value[PrayerName.SUNRISE] == true,
            duhrNotification = _uiSelectedPrayerAlarms.value[PrayerName.DUHR] == true,
            asrNotification = _uiSelectedPrayerAlarms.value[PrayerName.ASR] == true,
            maghribNotification = _uiSelectedPrayerAlarms.value[PrayerName.MAGRIB] == true,
            ishaNotification = _uiSelectedPrayerAlarms.value[PrayerName.ISHA] == true
        )
        updateSettings(currentSettings)

        if (_uiSwitchState.value) {
            viewModelScope.launch {
                val prayerAlarmWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>().build()
                workManager.enqueueUniqueWork(
                    PRAYER_ALARM_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    prayerAlarmWorkRequest
                )
                Log.i("ngz_alarms", "alarms scheduled")
            }
        } else {
            viewModelScope.launch {
                cancelAllPrayerAlarms()
                workManager.cancelUniqueWork(PRAYER_ALARM_WORK_NAME)
                workManager.cancelUniqueWork(PRAYER_ALARM_WORK_NEXT_DAY_NAME)
                Log.i("ngz_alarms", "alarms cancelled")
            }
        }
    }

    private fun updateSettings(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(settingsEntity)
        }
    }

    private fun saveRequestedPermission() {
        savedStateHandle[PENDING_ALARM_PERMISSION] = UiPermissionState.PENDING
    }

    private fun getPendingPermission(): UiPermissionState =
        savedStateHandle[PENDING_ALARM_PERMISSION] ?: UiPermissionState.NOT_REQUESTED

    private fun removePendingPermission() {
        savedStateHandle.remove<UiPermissionState>(PENDING_ALARM_PERMISSION)
    }

    private fun loadSavedSettings() {
        viewModelScope.launch {
            val settings = repository.getSettings()
            _uiSelectedRadio.update { settings.notificationType }

            if (alarmManager.canScheduleAlarms()) {
                _uiSwitchState.update { settings.notification }
            } else {
                _uiSwitchState.update { false }
            }
        }
    }

    private suspend fun cancelAllPrayerAlarms() {
        val timePrayer = repository.getPrayer(todayDate()).toTimePrayer()
        PrayerName.entries.forEach { prayerName ->
            val prayer = prayerCalculation.getNextPrayerLocalTime(
                timePrayer.getTimePrayerByName(prayerName)
            )
            val upcoming = prayer?.isAfter(LocalDateTime.now()) == true
            if (upcoming) {
                Log.i("ngz_alarms", "$prayerName alarm cancelled")
                alarmManager.cancelAlarm( // TODO: 1. replace test strings with notification strings.
                    AlarmItem(
                        time = prayer,
                        title = "${prayerName.name} Prayer",
                        message = "Time for ${prayerName.name} prayer."
                    )
                )
            }
        }
    }

    companion object {
        private const val PENDING_ALARM_PERMISSION = "pendingAlarmPermission"
    }
}
