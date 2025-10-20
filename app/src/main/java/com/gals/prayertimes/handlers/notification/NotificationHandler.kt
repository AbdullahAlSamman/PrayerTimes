package com.gals.prayertimes.handlers.notification

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
import androidx.core.net.toUri
import com.gals.prayertimes.R
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.getStringId
import com.gals.prayertimes.permissions.manager.notification.NotificationPermissionHandler
import com.gals.prayertimes.utils.ResourceProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class NotificationHandler @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val resourceProvider: ResourceProvider,
    private val notificationPermissionHandler: NotificationPermissionHandler
) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showAlarmNotification(
        prayer: UiPrayerName,
        notificationType: NotificationType,
        pendingIntent: PendingIntent
    ) {
        if (notificationPermissionHandler.isGranted()) {
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

    private fun buildNotification(
        pendingIntent: PendingIntent,
        prayer: UiPrayerName,
        notificationType: NotificationType
    ): Notification {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            notificationManager = notificationManager,
            notificationType = notificationType
        )

        return NotificationCompat.Builder(
            applicationContext,
            notificationType.getNotificationChannelID()
        )
            .setSmallIcon(R.drawable.ic_haya_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildNotificationInfo(prayer))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        notificationType: NotificationType
    ) {
        if (notificationManager.getNotificationChannel(notificationType.getNotificationChannelID()) == null) {
            val notificationChannel = NotificationChannel(
                notificationType.getNotificationChannelID(),
                notificationType.getNotificationChannelName(),
                NotificationManager.IMPORTANCE_HIGH
            )

            Timber.i("Sound Resource: ${notificationType.getNotificationSound()}")
            notificationChannel.setSound(
                notificationType.getNotificationSound(),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun buildNotificationInfo(prayer: UiPrayerName): RemoteViews =
        RemoteViews(
            applicationContext.packageName,
            R.layout.notification_service_alarm_remote_view
        ).apply {
            setTextViewText(
                R.id.notification_permanent_next_prayer_banner_text,
                when (prayer) {
                    UiPrayerName.SUNRISE -> resourceProvider.getString(R.string.text_now_time_sunrise)
                    else -> resourceProvider.getString(R.string.text_now_prayer_time)
                }
            )

            setTextViewText(
                R.id.notification_alarm_next_prayer_text,
                resourceProvider.getString(prayer.getStringId())
            )
        }

    private fun NotificationType.getNotificationSound(): Uri =
        when (this) {
            NotificationType.SILENT -> Uri.EMPTY
            NotificationType.TONE -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            NotificationType.HALF -> (URI_DEFAULT_PATH + R.raw.halfathan).toUri()
            NotificationType.FULL -> (URI_DEFAULT_PATH + R.raw.fullathan).toUri()
        }

    private fun NotificationType.getNotificationChannelID(): String =
        when (this) {
            NotificationType.SILENT -> NOTIFICATION_CHANNEL_ID_SILENT_ATHAN_ALARM
            NotificationType.TONE -> NOTIFICATION_CHANNEL_ID_RINGTONE_ATHAN_ALARM
            NotificationType.HALF -> NOTIFICATION_CHANNEL_ID_HALF_ATHAN_ALARM
            NotificationType.FULL -> NOTIFICATION_CHANNEL_ID_FULL_ATHAN_ALARM
        }

    private fun NotificationType.getNotificationChannelName(): String =
        when (this) {
            NotificationType.SILENT -> NOTIFICATION_CHANNEL_NAME_ALARM_SILENT
            NotificationType.TONE -> NOTIFICATION_CHANNEL_NAME_ALARM_RINGTONE
            NotificationType.HALF -> NOTIFICATION_CHANNEL_NAME_ALARM_HALF_ATHAN
            NotificationType.FULL -> NOTIFICATION_CHANNEL_NAME_ALARM_FULL_ATHAN
        }

    companion object {
        private const val URI_DEFAULT_PATH = "android.resource://com.gals.prayertimes/"
        private const val NOTIFICATION_CHANNEL_ID_FULL_ATHAN_ALARM =
            "athan_notification_full_channel_permanent"
        private const val NOTIFICATION_CHANNEL_ID_HALF_ATHAN_ALARM =
            "athan_notification_half_channel_permanent"
        private const val NOTIFICATION_CHANNEL_ID_RINGTONE_ATHAN_ALARM =
            "athan_notification_ringtone_channel_permanent"
        private const val NOTIFICATION_CHANNEL_ID_SILENT_ATHAN_ALARM =
            "athan_notification_silent_channel_permanent"
        private const val NOTIFICATION_CHANNEL_NAME_ALARM_FULL_ATHAN = "Full Athan Alarm"
        private const val NOTIFICATION_CHANNEL_NAME_ALARM_HALF_ATHAN = "Half Athan Alarm"
        private const val NOTIFICATION_CHANNEL_NAME_ALARM_RINGTONE = "Default Ringtone Alarm"
        private const val NOTIFICATION_CHANNEL_NAME_ALARM_SILENT = "Silent Alarm"
    }
}