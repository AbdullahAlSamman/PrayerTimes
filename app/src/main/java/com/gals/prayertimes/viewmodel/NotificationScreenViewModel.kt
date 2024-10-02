package com.gals.prayertimes.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.UiPermissionState
import com.gals.prayertimes.model.UiPrayerName
import com.gals.prayertimes.model.UiSelectedPrayerAlarm
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.services.alarmmanager.AlarmItem
import com.gals.prayertimes.services.alarmmanager.AlarmManager
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiSelectedRadio = MutableStateFlow(NotificationType.SILENT.value)
    private val _uiSwitchState = MutableStateFlow(false)
    private val _uiSelectedPrayerAlarms = MutableStateFlow(UiSelectedPrayerAlarm())
    private val _uiPermissionState = MutableStateFlow(getPendingPermission())

    val uiSelectedRadio: StateFlow<String> = _uiSelectedRadio.asStateFlow()
    val uiSwitchState: StateFlow<Boolean> = _uiSwitchState.asStateFlow()
    val uiPermissionState: StateFlow<UiPermissionState> = _uiPermissionState.asStateFlow()
    val uiSelectedPrayerAlarms: StateFlow<UiSelectedPrayerAlarm> = _uiSelectedPrayerAlarms.asStateFlow()

    init {
        checkSettingsAndPermissions()
    }

    fun updateSelectedRadio(value: String) {
        _uiSelectedRadio.update { value }
        updateSettings(
            SettingsEntity(notificationType = value, notification = _uiSwitchState.value)
        )
    }

    fun updatePermissionState(value: UiPermissionState) {
        _uiPermissionState.update { value }
        when (value) {
            UiPermissionState.GRANTED, UiPermissionState.DENIED -> {
                removePendingPermission()
            }

            else -> {}
        }
    }

    fun updateSwitchState(value: Boolean) {
        when (value) {
            true -> {
                if (alarmManager.canScheduleAlarms()) {
                    turnOnAlarm()
                } else {
                    _uiPermissionState.update { UiPermissionState.REQUESTED }
                }
            }

            false -> {
                _uiSwitchState.update { false }

                alarmManager.cancelAlarm(
                    AlarmItem(
                        time = LocalDateTime.now().plusMinutes(1),
                        title = "test title",
                        message = "test message"
                    )
                )

                updateSettings(
                    SettingsEntity(notificationType = _uiSelectedRadio.value, notification = false)
                )
            }
        }
    }

    fun updateSelectedAlarms(name: UiPrayerName, value: Boolean) {
        val updatedSelection = _uiSelectedPrayerAlarms.value.selection
        updatedSelection[name] = value
        _uiSelectedPrayerAlarms.update { UiSelectedPrayerAlarm(updatedSelection) }
    }

    fun requestAlarmPermission() {
        saveRequestedPermission()
        alarmManager.requestPermission()
    }

    fun getPendingPermissions(): UiPermissionState = getPendingPermission()

    fun isAlarmPermissionGranted(): Boolean = alarmManager.canScheduleAlarms()

    private fun turnOnAlarm() {
        _uiSwitchState.update { true }

        viewModelScope.launch {
            alarmManager.scheduleAlarm(
                AlarmItem(
                    time = LocalDateTime.now().plusMinutes(1),
                    title = "test title",
                    message = "test message"
                )
            )
        }

        updateSettings(
            SettingsEntity(
                notificationType = _uiSelectedRadio.value,
                notification = true
            )
        )
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

    private fun checkSettingsAndPermissions() {
        viewModelScope.launch {
            val settings = repository.getSettings()
            if (alarmManager.canScheduleAlarms()) {
                _uiSwitchState.update { settings.notification }
                _uiSelectedRadio.update { settings.notificationType }
            } else {
                updateSettings(
                    SettingsEntity(
                        notificationType = settings.notificationType,
                        notification = false
                    )
                )
                _uiSwitchState.update { false }
                _uiSelectedRadio.update { settings.notificationType }
            }
        }
    }

    companion object {
        private const val PENDING_ALARM_PERMISSION = "pendingAlarmPermission"
    }
}