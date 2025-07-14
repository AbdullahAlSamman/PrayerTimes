package com.gals.prayertimes.services.notificationmanager

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.widget.RemoteViews
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.model.mappers.getStringId
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.SystemUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

class NotificationManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val resourceProvider: ResourceProvider,
    private val utils: SystemUtils
) {

    /*TODO: Notification permission check refactor and cleanup notification manager*/
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showAlarmNotification(
        prayer: PrayerName,
        notificationType: NotificationType,
        pendingIntent: PendingIntent
    ) {
        if (utils.hasNotificationPermission()) {
            val notification = buildNotification(
                pendingIntent = pendingIntent,
                notificationType = notificationType,
                prayer = prayer
            )
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(Random.nextInt(0, Int.MAX_VALUE), notification)
            }
        }
    }

    private fun buildAlarmNotificationBannerInfo(prayer: PrayerName): RemoteViews =
        RemoteViews(
            applicationContext.packageName,
            R.layout.notification_service_alarm_remote_view
        ).apply {
            setTextViewText(
                R.id.notification_permanent_next_prayer_banner_text,
                when (prayer) {
                    PrayerName.SUNRISE -> resourceProvider.getString(R.string.text_now_time_sunrise)
                    else -> resourceProvider.getString(R.string.text_now_prayer_time)
                }
            )

            setTextViewText(
                R.id.notification_alarm_next_prayer_text,
                resourceProvider.getString(prayer.getStringId())
            )
        }

    private fun buildNotification(
        pendingIntent: PendingIntent,
        notificationType: NotificationType,
        prayer: PrayerName
    ) = when (notificationType) {
        NotificationType.SILENT -> createNotification(
            pendingIntent = pendingIntent,
            soundUri = notificationType.getNotificationSound(),
            prayer = prayer
        )

        NotificationType.TONE -> createNotification(
            pendingIntent = pendingIntent,
            soundUri = notificationType.getNotificationSound(),
            prayer = prayer
        )

        NotificationType.HALF -> createNotification(
            pendingIntent = pendingIntent,
            soundUri = notificationType.getNotificationSound(),
            prayer = prayer
        )

        NotificationType.FULL -> createNotification(
            pendingIntent = pendingIntent,
            soundUri = notificationType.getNotificationSound(),
            prayer = prayer
        )
    }

    private fun NotificationType.getNotificationSound(): Uri =
        when (this) {
            NotificationType.SILENT -> Uri.EMPTY
            NotificationType.TONE -> defaultRingtone
            NotificationType.HALF -> utils.getSoundUri(NotificationType.HALF)
            NotificationType.FULL -> utils.getSoundUri(NotificationType.FULL)
        }

    private fun createNotification(
        pendingIntent: PendingIntent,
        prayer: PrayerName,
        soundUri: Uri
    ): Notification {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ALARM_ID,
            NOTIFICATION_CHANNEL_ALARM_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.setSound(
            soundUri,
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )
        notificationManager.createNotificationChannel(notificationChannel)
        return createNotificationWithSound(pendingIntent, prayer)
    }

    private fun createNotificationWithSound(
        pendingIntent: PendingIntent,
        prayer: PrayerName
    ): Notification =
        NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ALARM_ID)
            .setSmallIcon(R.drawable.ic_haya_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildAlarmNotificationBannerInfo(prayer))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

    private val defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    companion object {
        private const val NOTIFICATION_CHANNEL_ALARM_ID = "athan_notification_channel_permanent"
        private const val NOTIFICATION_CHANNEL_ALARM_NAME = "Athan Alarm"
    }
}