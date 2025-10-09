package com.gals.prayertimes.services.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import com.gals.prayertimes.common.mappers.toAlarmItem
import com.gals.prayertimes.utils.upAPILevel31
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class AlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(prayerAlarmItem: PrayerAlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(INTENT_EXTRA_NOTIFICATION_TYPE, prayerAlarmItem.notificationType)
            putExtra(INTENT_EXTRA_NOTIFICATION_PRAYER, prayerAlarmItem.prayer)
        }
        if (upAPILevel31) {
            when {
                alarmManager.canScheduleExactAlarms() -> {
                    setAlarm(prayerAlarmItem.toAlarmItem(), intent)
                }

                else -> {
                    requestPermission()
                }
            }
        } else {
            setAlarm(prayerAlarmItem.toAlarmItem(), intent)
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
        if (upAPILevel31) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

    fun requestPermission() {
        if (upAPILevel31) {
            context.startActivity(
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
}

const val INTENT_EXTRA_NOTIFICATION_TYPE = "notificationType"
const val INTENT_EXTRA_NOTIFICATION_PRAYER = "notificationPrayer"