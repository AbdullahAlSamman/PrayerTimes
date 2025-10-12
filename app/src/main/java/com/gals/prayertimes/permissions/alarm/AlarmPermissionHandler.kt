package com.gals.prayertimes.permissions.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.gals.prayertimes.permissions.PermissionHandler
import com.gals.prayertimes.utils.upAPILevel31
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Handles the "Schedule exact alarm" permission for Android 12 and above.
 */
class AlarmPermissionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) : PermissionHandler {

    override fun isGranted(): Boolean = if (upAPILevel31) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }

    override fun requestPermission() {
        if (upAPILevel31) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}
