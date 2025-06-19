package com.gals.prayertimes.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.UiPermissionState
import com.gals.prayertimes.model.UiPrayerName
import com.gals.prayertimes.model.mappers.toTimePrayer
import com.gals.prayertimes.model.mappers.todayDate
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.services.alarmmanager.AlarmItem
import com.gals.prayertimes.services.alarmmanager.AlarmManager
import com.gals.prayertimes.utils.PrayerCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val alarmManager: AlarmManager,
    private val prayerCalculation: PrayerCalculation,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiSelectedRadio = MutableStateFlow(NotificationType.SILENT.value)
    private val _uiSwitchState = MutableStateFlow(false)
    private val _uiSelectedPrayerAlarms =
        MutableStateFlow(UiPrayerName.entries.associateWith { it != UiPrayerName.SUNRISE })
    private val _uiPermissionState = MutableStateFlow(getPendingPermission())

    val uiSelectedRadio: StateFlow<String> = _uiSelectedRadio.asStateFlow()
    val uiSwitchState: StateFlow<Boolean> = _uiSwitchState.asStateFlow()
    val uiPermissionState: StateFlow<UiPermissionState> = _uiPermissionState.asStateFlow()
    val uiSelectedPrayerAlarms: StateFlow<Map<UiPrayerName, Boolean>> =
        _uiSelectedPrayerAlarms.asStateFlow()

    init {
        checkSettingsAndPermissions()
    }

    private suspend fun rescheduleAllSelectedAlarms() {
        if (!_uiSwitchState.value || !alarmManager.canScheduleAlarms()) return

        val timePrayer = repository.getPrayer(todayDate()).toTimePrayer()
        _uiSelectedPrayerAlarms.value.forEach { prayerName, isSelected ->
            if (isSelected && prayerName != UiPrayerName.SUNRISE) {
                prayerCalculation.getNextPrayerLocalDateTime(prayerName, timePrayer)
                    ?.let { prayerDateTime ->
                        alarmManager.scheduleAlarm(
                            AlarmItem(
                                time = prayerDateTime,
                                title = "${prayerName.name} Prayer",
                                message = "Time for ${prayerName.name} prayer."
                            )
                        )
                    }
            }
        }
    }

    private suspend fun cancelAllPrayerAlarms() {
        val timePrayer = repository.getPrayer(todayDate()).toTimePrayer()
        UiPrayerName.entries.forEach { prayerName ->
            if (prayerName != UiPrayerName.SUNRISE) { // No need to cancel sunrise if never scheduled
                prayerCalculation.getNextPrayerLocalDateTime(prayerName, timePrayer)
                    ?.let { prayerDateTime ->
                        alarmManager.cancelAlarm(
                            AlarmItem(
                                time = prayerDateTime,
                                title = "${prayerName.name} Prayer",
                                message = "Time for ${prayerName.name} prayer."
                            )
                        )
                    }
            }
        }
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
            UiPermissionState.GRANTED -> {
                removePendingPermission()
                // If permission just granted, and switch was supposed to be on, reschedule
                if (_uiSwitchState.value) {
                    viewModelScope.launch { rescheduleAllSelectedAlarms() }
                }
            }

            UiPermissionState.DENIED -> {
                removePendingPermission()
                // If permission denied, ensure alarms are cancelled and switch is off
                _uiSwitchState.update { false }
                viewModelScope.launch { cancelAllPrayerAlarms() }
                updateSettings(
                    SettingsEntity(notificationType = _uiSelectedRadio.value, notification = false)
                )
            }

            else -> {}
        }
    }

    fun updateSwitchState(value: Boolean) {
        if (value) { // Turning ON
            if (alarmManager.canScheduleAlarms()) {
                _uiSwitchState.update { true }
                viewModelScope.launch {
                    rescheduleAllSelectedAlarms()
                }
                updateSettings(
                    SettingsEntity(
                        notificationType = _uiSelectedRadio.value,
                        notification = true
                    )
                )
            } else {
                _uiPermissionState.update { UiPermissionState.REQUESTED }
            }
        } else { // Turning OFF
            _uiSwitchState.update { false }
            viewModelScope.launch {
                cancelAllPrayerAlarms()
            }
            updateSettings(
                SettingsEntity(notificationType = _uiSelectedRadio.value, notification = false)
            )
        }
    }

    fun updateSelectedAlarms(prayerName: UiPrayerName, isSelected: Boolean) {
        _uiSelectedPrayerAlarms.update { oldMap -> oldMap + (prayerName to isSelected) }

        if (!_uiSwitchState.value || !alarmManager.canScheduleAlarms() || prayerName == UiPrayerName.SUNRISE) {
            return
        }

        viewModelScope.launch {
            val timePrayer = repository.getPrayer(todayDate()).toTimePrayer()
            prayerCalculation.getNextPrayerLocalDateTime(prayerName, timePrayer)
                ?.let { prayerDateTime ->
                    val alarmItem = AlarmItem(
                        time = prayerDateTime,
                        title = "${prayerName.name} Prayer",
                        message = "Time for ${prayerName.name} prayer."
                    )
                    if (isSelected) {
                        alarmManager.scheduleAlarm(alarmItem)
                    } else {
                        alarmManager.cancelAlarm(alarmItem)
                    }
                }
        }
        // TODO: Persist the state of _uiSelectedPrayerAlarms if needed.
    }

    fun requestAlarmPermission() {
        saveRequestedPermission()
        alarmManager.requestPermission()
    }

    fun getPendingPermissions(): UiPermissionState = getPendingPermission()

    fun isAlarmPermissionGranted(): Boolean = alarmManager.canScheduleAlarms()

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
            _uiSelectedRadio.update { settings.notificationType }

            if (alarmManager.canScheduleAlarms()) {
                _uiSwitchState.update { settings.notification }
                if (settings.notification) {
                    rescheduleAllSelectedAlarms()
                } else {
                    // This case might be redundant if alarms are always cancelled when switch is off
                    // but it's safer to ensure they are cancelled if settings.notification is false.
                    cancelAllPrayerAlarms()
                }
            } else {
                _uiSwitchState.update { false } // Ensure switch is off if no permission
                cancelAllPrayerAlarms() // Cancel any existing alarms if permission is lost
                // Persist the switch state as false if permission is not granted
                updateSettings(
                    SettingsEntity(
                        notificationType = settings.notificationType,
                        notification = false
                    )
                )
            }
        }
    }

    companion object {
        private const val PENDING_ALARM_PERMISSION = "pendingAlarmPermission"
    }
}
