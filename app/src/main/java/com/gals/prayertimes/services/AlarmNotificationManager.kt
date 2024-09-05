package com.gals.prayertimes.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class AlarmNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(NOTIFICATION_MESSAGE, alarmItem.message)
            putExtra(NOTIFICATION_TITLE, alarmItem.title)
        }
        if (checkAPILevel) {
            when {
                alarmManager.canScheduleExactAlarms() -> {
                    setAlarm(alarmItem, intent)
                }

                else -> {
                    requestPermission()
                }
            }
        } else {
            setAlarm(alarmItem, intent)
        }
    }

    fun cancelAlarm(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun canScheduleAlarms(): Boolean =
        if (checkAPILevel) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

    fun requestPermission() {
        if (checkAPILevel) {
            startActivity(
                context,
                Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                null
            )
        }
    }

    private fun setAlarm(alarmItem: AlarmItem, intent: Intent) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmItem.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private val checkAPILevel: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

const val NOTIFICATION_MESSAGE = "notificationMessage"
const val NOTIFICATION_TITLE = "notificationTitle"