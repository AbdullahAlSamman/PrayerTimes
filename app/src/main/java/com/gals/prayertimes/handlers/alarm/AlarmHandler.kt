package com.gals.prayertimes.handlers.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.gals.prayertimes.common.mappers.toAlarmItem
import com.gals.prayertimes.utils.upAPILevel31
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class AlarmHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) {
    fun scheduleAlarm(prayerAlarmItem: PrayerAlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(INTENT_EXTRA_NOTIFICATION_TYPE, prayerAlarmItem.notificationType)
            putExtra(INTENT_EXTRA_NOTIFICATION_PRAYER, prayerAlarmItem.prayer)
        }
        setAlarm(prayerAlarmItem.toAlarmItem(), intent)
    }

    fun cancelAlarm(alarmItem: AlarmItem) {
        try {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    alarmItem.hashCode(),
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } catch (e: Exception) {
            Timber.e(e.stackTrace.toString())
        } finally {
            Timber.i("Alarm cancelled for: ${alarmItem.prayer}, hash: ${alarmItem.hashCode()}")
        }
    }

    private fun canScheduleAlarms(): Boolean =
        if (upAPILevel31) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

    private fun setAlarm(alarmItem: AlarmItem, intent: Intent) {
        if (canScheduleAlarms()) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmItem.time,
                    PendingIntent.getBroadcast(
                        context,
                        alarmItem.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                Timber.i("Alarm set for: ${alarmItem.prayer}, hash: ${alarmItem.hashCode()}")
            }
        }
    }
}

const val INTENT_EXTRA_NOTIFICATION_TYPE = "notificationType"
const val INTENT_EXTRA_NOTIFICATION_PRAYER = "notificationPrayer"