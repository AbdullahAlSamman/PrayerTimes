package com.gals.prayertimes.permissions

import androidx.lifecycle.ViewModel
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
    private val notificationPermissionHandler: NotificationPermissionHandler,
    private val alarmPermissionHandler: AlarmPermissionHandler,
    private val batteryPermissionHandler: BatteryOptimizationPermissionHandler
) : ViewModel() {
    //TODO add loading and content state while checking permissions
    private val _uiPermissions =
        MutableStateFlow(PermissionType.entries.associateWith { UiPermissionState.Required })
    val uiPermissionStates: StateFlow<Map<PermissionType, UiPermissionState>> =
        _uiPermissions.asStateFlow()

    fun requestPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.Notification -> notificationPermissionHandler.requestPermission()
            PermissionType.BatteryOptimisation -> batteryPermissionHandler.requestPermission()
            PermissionType.Alarm -> alarmPermissionHandler.requestPermission()
        }
    }

    fun openSettings(permission: PermissionType) {
        when (permission) {
            PermissionType.Notification -> notificationPermissionHandler.openSettings()
            PermissionType.BatteryOptimisation -> batteryPermissionHandler.openSettings()
            PermissionType.Alarm -> alarmPermissionHandler.openSettings()
        }
    }

    fun checkPermissions() {
        for (permission in PermissionType.entries) {
            when (permission) {
                PermissionType.Notification ->
                    _uiPermissions.update { currentPermissions ->
                        currentPermissions + (PermissionType.Notification to if (notificationPermissionHandler.isGranted()) UiPermissionState.NotRequired else UiPermissionState.Required)
                    }

                PermissionType.BatteryOptimisation ->
                    _uiPermissions.update { currentPermissions ->
                        currentPermissions + (PermissionType.BatteryOptimisation to if (batteryPermissionHandler.isGranted()) UiPermissionState.NotRequired else UiPermissionState.Required)
                    }

                PermissionType.Alarm ->
                    _uiPermissions.update { currentPermissions ->
                        currentPermissions + (PermissionType.Alarm to if (alarmPermissionHandler.isGranted()) UiPermissionState.NotRequired else UiPermissionState.Required)
                    }
            }
        }
    }
}
