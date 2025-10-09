package com.gals.prayertimes.services.alarmmanager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.main.MainActivity
import com.gals.prayertimes.services.notificationmanagement.NotificationManagement
import com.gals.prayertimes.utils.SystemUtils
import com.gals.prayertimes.utils.upAPILevel31
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManagement: NotificationManagement

    @Inject
    lateinit var utils: SystemUtils

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            val notificationType =
                NotificationType.fromString(intent?.getStringExtra(INTENT_EXTRA_NOTIFICATION_TYPE))
            val prayerName =
                UiPrayerName.fromString(intent?.getStringExtra(INTENT_EXTRA_NOTIFICATION_PRAYER))

            Timber.i("Alarm received for: $prayerName, type: $notificationType")

            val intent = Intent(context, MainActivity::class.java)

            val tapIntent = PendingIntent.getActivity(
                context,
                prayerName.hashCode(),
                intent,
                if (upAPILevel31)
                    PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (utils.hasNotificationPermission()) {
                @Suppress("MissingPermission")
                notificationManagement.showAlarmNotification(
                    pendingIntent = tapIntent,
                    notificationType = notificationType,
                    prayer = prayerName
                )
            }

            Timber.i("Notification should be shown for $prayerName")
        }
    }
}