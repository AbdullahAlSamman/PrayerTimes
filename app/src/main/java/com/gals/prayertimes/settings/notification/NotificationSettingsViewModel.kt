package com.gals.prayertimes.settings.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.getTimePrayerByName
import com.gals.prayertimes.common.mappers.toMillisecondsWithRoundedSeconds
import com.gals.prayertimes.common.mappers.toTimePrayer
import com.gals.prayertimes.handlers.alarm.AlarmHandler
import com.gals.prayertimes.handlers.alarm.AlarmItem
import com.gals.prayertimes.handlers.alarm.AlarmWorker
import com.gals.prayertimes.handlers.alarm.PRAYER_ALARM_WORK_NAME
import com.gals.prayertimes.handlers.alarm.PRAYER_ALARM_WORK_NEXT_DAY_NAME
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.repository.SettingsRepository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity.Companion.toPrayerNotification
import com.gals.prayertimes.settings.notification.tracker.NotificationSettingsTracker
import com.gals.prayertimes.settings.notification.tracker.mapper.toTrackingParameters
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.todayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val settingsRepository: SettingsRepository,
    private val alarmHandler: AlarmHandler,
    private val prayerCalculation: PrayerCalculation,
    private val workManager: WorkManager,
    private val tracker: NotificationSettingsTracker
) : ViewModel() {
    private val _uiSelectedRadio = MutableStateFlow(NotificationType.SILENT)
    private val _uiSwitchState = MutableStateFlow(false)
    private val _uiSelectedPrayerAlarms =
        MutableStateFlow(UiPrayerName.entries.associateWith { it != UiPrayerName.SUNRISE })

    val uiSelectedRadio: StateFlow<NotificationType> = _uiSelectedRadio.asStateFlow()
    val uiSwitchState: StateFlow<Boolean> = _uiSwitchState.asStateFlow()
    val uiSelectedPrayerAlarms: StateFlow<Map<UiPrayerName, Boolean>> =
        _uiSelectedPrayerAlarms.asStateFlow()

    init {
        loadSavedSettings()
    }

    fun updateSelectedRadio(value: NotificationType) {
        _uiSelectedRadio.update { value }
    }

    fun updateSwitchState(value: Boolean) =
        if (value) {
            _uiSwitchState.update { true }
        } else {
            _uiSwitchState.update { false }
        }

    fun updateSelectedAlarms(prayerName: UiPrayerName, isSelected: Boolean) {
        _uiSelectedPrayerAlarms.update { oldMap -> oldMap + (prayerName to isSelected) }
    }

    fun submitChanges() {
        val currentSettings = SettingsEntity(
            notification = _uiSwitchState.value,
            notificationType = uiSelectedRadio.value,
            fajerNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.FAJER] == true,
            sunriseNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.SUNRISE] == true,
            duhrNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.DUHR] == true,
            asrNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.ASR] == true,
            maghribNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.MAGHRIB] == true,
            ishaNotification = _uiSelectedPrayerAlarms.value[UiPrayerName.ISHA] == true
        )
        updateSettings(currentSettings)
        tracker.submitSettings(currentSettings.toTrackingParameters())

        viewModelScope.launch {
            cancelAllPrayerAlarms()
            workManager.cancelUniqueWork(PRAYER_ALARM_WORK_NAME)
            workManager.cancelUniqueWork(PRAYER_ALARM_WORK_NEXT_DAY_NAME)
            Timber.Forest.i("All alarms are cancelled")

            if (_uiSwitchState.value) {
                val prayerAlarmWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>().build()
                workManager.enqueueUniqueWork(
                    PRAYER_ALARM_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    prayerAlarmWorkRequest
                )
                Timber.Forest.i("alarms are scheduled")
            }
        }
    }

    private fun updateSettings(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            settingsRepository.saveSettings(settingsEntity)
        }
    }

    private fun loadSavedSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSavedSettings()
            _uiSelectedRadio.update { settings.notificationType }
            _uiSelectedPrayerAlarms.update { settings.toPrayerNotification() }
            _uiSwitchState.update { settings.notification }
        }
    }

    private suspend fun cancelAllPrayerAlarms() {
        prayersRepository.getLocalPrayer(todayDate()).map { it.toTimePrayer() }
            .collect { timePrayer ->
                UiPrayerName.entries.forEach { prayerName ->
                    val prayerTime = prayerCalculation.getNextPrayerLocalTime(
                        timePrayer.getTimePrayerByName(prayerName)
                    )
                    val upcoming = prayerTime?.isAfter(LocalDateTime.now()) == true
                    if (upcoming) {
                        alarmHandler.cancelAlarm(
                            AlarmItem(
                                time = prayerTime.toMillisecondsWithRoundedSeconds(),
                                prayer = prayerName.name
                            )
                        )
                    }
                }
            }
    }
}