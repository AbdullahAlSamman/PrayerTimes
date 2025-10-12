package com.gals.prayertimes.permissions

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gals.prayertimes.handlers.alarm.AlarmHandler
import com.gals.prayertimes.permissions.alarm.AlarmPermissionHandler
import com.gals.prayertimes.permissions.battery.BatteryOptimizationPermissionHandler
import com.gals.prayertimes.permissions.notification.NotificationPermissionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val notificationPermissionHandler: NotificationPermissionHandler,
    private val alarmManager: AlarmHandler,
    private val alarmPermissionHandler: AlarmPermissionHandler,
    private val batteryPermissionHandler: BatteryOptimizationPermissionHandler,
    private val savedStateHandle: SavedStateHandle //TODO check if required for the permission
) : ViewModel() {
    private val _uiPermissions = MutableStateFlow(emptyMap<UiPermission, UiPermissionState>())
    val uiPermissionStates: StateFlow<Map<UiPermission, UiPermissionState>> =
        _uiPermissions.asStateFlow()

    init {
        checkPermissions(context = context, uiPermissions = _uiPermissions)
    }

    fun updatePermissionState(key: UiPermission, value: UiPermissionState) {
        _uiPermissions.update { it + (key to value) }
    }

    fun requestExactAlarmPermission() {
        saveRequestedPermission()
        alarmPermissionHandler.requestPermission()
    }

    fun getPendingPermissions(): UiPermissionState = getPendingPermission()

    private fun checkPermissions(
        context: Context,
        uiPermissions: MutableStateFlow<Map<UiPermission, UiPermissionState>>
    ) {
        uiPermissions.value.forEach { (key, _) ->
            when (key) {
                UiPermission.NOTIFICATION -> {
                    if (!notificationPermissionHandler.isGranted()) {
                        uiPermissions.update { it + (key to UiPermissionState.REQUIRED) }
                    }
                }

                UiPermission.BATTERY_OPTIMIZATION -> {
                    if (!batteryPermissionHandler.isGranted()) {
                        uiPermissions.update { it + (key to UiPermissionState.REQUIRED) }
                    }
                }

                UiPermission.ALARM -> {
                    if (!alarmManager.canScheduleAlarms()) {
                        uiPermissions.update { it + (key to UiPermissionState.REQUIRED) }
                    }
                }
            }
        }
    }

    private fun saveRequestedPermission() {
        savedStateHandle[PENDING_ALARM_PERMISSION] = UiPermissionState.PENDING
    }

    private fun getPendingPermission(): UiPermissionState =
        savedStateHandle[PENDING_ALARM_PERMISSION] ?: UiPermissionState.NOT_REQUIRED

    private fun removePendingPermission() {
        savedStateHandle.remove<UiPermissionState>(PENDING_ALARM_PERMISSION)
    }

    companion object {
        private const val PENDING_ALARM_PERMISSION = "pendingAlarmPermission"
    }
}