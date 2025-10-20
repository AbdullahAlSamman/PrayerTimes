package com.gals.prayertimes.permissions.manager

import com.gals.prayertimes.permissions.manager.alarm.AlarmPermissionHandler
import com.gals.prayertimes.permissions.manager.battery.BatteryOptimizationPermissionHandler
import com.gals.prayertimes.permissions.manager.notification.NotificationPermissionHandler
import com.gals.prayertimes.permissions.model.PermissionState
import com.gals.prayertimes.permissions.model.PermissionType
import javax.inject.Inject

class PermissionsManager @Inject constructor(
    private val alarmPermissionHandler: AlarmPermissionHandler,
    private val notificationPermissionHandler: NotificationPermissionHandler,
    private val batteryPermissionHandler: BatteryOptimizationPermissionHandler
) {

    fun areAllPermissionsGranted(): Boolean =
        alarmPermissionHandler.isGranted() && notificationPermissionHandler.isGranted() && batteryPermissionHandler.isGranted()


    fun openSettings(permission: PermissionType) {
        when (permission) {
            PermissionType.Notification -> notificationPermissionHandler.openSettings()
            PermissionType.BatteryOptimisation -> batteryPermissionHandler.openSettings()
            PermissionType.Alarm -> alarmPermissionHandler.openSettings()
        }
    }

    fun requestPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.Notification -> notificationPermissionHandler.requestPermission()
            PermissionType.BatteryOptimisation -> batteryPermissionHandler.requestPermission()
            PermissionType.Alarm -> alarmPermissionHandler.requestPermission()
        }
    }

    fun checkPermission(permission: PermissionType): Pair<PermissionType, PermissionState> =
        when (permission) {
            PermissionType.Notification -> (PermissionType.Notification to if (notificationPermissionHandler.isGranted()) PermissionState.NotRequired else PermissionState.Required)
            PermissionType.BatteryOptimisation -> (PermissionType.BatteryOptimisation to if (batteryPermissionHandler.isGranted()) PermissionState.NotRequired else PermissionState.Required)
            PermissionType.Alarm -> (PermissionType.Alarm to if (alarmPermissionHandler.isGranted()) PermissionState.NotRequired else PermissionState.Required)
        }
}