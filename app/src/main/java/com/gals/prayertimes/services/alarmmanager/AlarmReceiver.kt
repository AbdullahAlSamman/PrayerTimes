package com.gals.prayertimes.services.alarmmanager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.services.notificationmanager.NotificationManager
import com.gals.prayertimes.utils.SystemUtils
import com.gals.prayertimes.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var utils: SystemUtils

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            val notificationType =
                NotificationType.fromString(intent?.getStringExtra(INTENT_EXTRA_NOTIFICATION_TYPE))
            val prayerName = PrayerName.fromString(intent?.getStringExtra(INTENT_EXTRA_NOTIFICATION_PRAYER))

            Log.i("ngz_notification", "Alarm received for: $prayerName, type: $notificationType")

            val intent = Intent(context, MainActivity::class.java)
            val mutabilityFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val tapIntent = PendingIntent.getActivity(
                context,
                prayerName.hashCode(),
                intent,
                mutabilityFlag or PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (utils.hasNotificationPermission()) {
                @Suppress("MissingPermission")
                notificationManager.showAlarmNotification(
                    pendingIntent = tapIntent,
                    notificationType = notificationType,
                    prayer = prayerName
                )
            }

            Log.i(
                "ngz_notification",
                "Notification should be shown for $prayerName"
            )
        }
    }
}