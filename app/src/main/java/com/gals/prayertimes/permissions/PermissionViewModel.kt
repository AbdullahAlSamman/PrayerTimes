package com.gals.prayertimes.permissions

import androidx.lifecycle.ViewModel
import com.gals.prayertimes.permissions.PermissionScreen.PermissionState.NotRequired
import com.gals.prayertimes.permissions.PermissionScreen.PermissionState.Required
import com.gals.prayertimes.permissions.alarm.AlarmPermissionHandler
import com.gals.prayertimes.permissions.battery.BatteryOptimizationPermissionHandler
import com.gals.prayertimes.permissions.notification.NotificationPermissionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val alarmPermissionHandler: AlarmPermissionHandler,
    private val notificationPermissionHandler: NotificationPermissionHandler,
    private val batteryPermissionHandler: BatteryOptimizationPermissionHandler
) : ViewModel() {
    private val _uiState = MutableStateFlow<PermissionScreen.State>(PermissionScreen.State.Loading)
    val uiState: StateFlow<PermissionScreen.State> = _uiState.asStateFlow()

    init {
        checkPermissions()
    }

    fun requestPermission(permission: PermissionScreen.PermissionType) {
        when (permission) {
            PermissionScreen.PermissionType.Notification -> notificationPermissionHandler.requestPermission()
            PermissionScreen.PermissionType.BatteryOptimisation -> batteryPermissionHandler.requestPermission()
            PermissionScreen.PermissionType.Alarm -> alarmPermissionHandler.requestPermission()
        }
    }

    fun openSettings(permission: PermissionScreen.PermissionType) {
        when (permission) {
            PermissionScreen.PermissionType.Notification -> notificationPermissionHandler.openSettings()
            PermissionScreen.PermissionType.BatteryOptimisation -> batteryPermissionHandler.openSettings()
            PermissionScreen.PermissionType.Alarm -> alarmPermissionHandler.openSettings()
        }
    }

    fun updatePermissions() {
        _uiState.update { PermissionScreen.State.Loading }
        checkPermissions()
    }

    private fun checkPermissions() {
        _uiState.update {
            PermissionScreen.State.Content(
                permissions = PermissionScreen.PermissionType.entries.associate { it.checkPermission() }
            )
        }
    }

    private fun PermissionScreen.PermissionType.checkPermission(): Pair<PermissionScreen.PermissionType, PermissionScreen.PermissionState> =
        when (this) {
            PermissionScreen.PermissionType.Notification -> (PermissionScreen.PermissionType.Notification to if (notificationPermissionHandler.isGranted()) NotRequired else Required)
            PermissionScreen.PermissionType.BatteryOptimisation -> (PermissionScreen.PermissionType.BatteryOptimisation to if (batteryPermissionHandler.isGranted()) NotRequired else Required)
            PermissionScreen.PermissionType.Alarm -> (PermissionScreen.PermissionType.Alarm to if (alarmPermissionHandler.isGranted()) NotRequired else Required)
        }
}
