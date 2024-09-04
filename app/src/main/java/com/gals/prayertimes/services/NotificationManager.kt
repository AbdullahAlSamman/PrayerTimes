package com.gals.prayertimes.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.view.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule
import kotlin.random.Random

class NotificationManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val repository: Repository,
    private val calculation: PrayerCalculation,
    private val tools: UtilsManager,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + Job())
) {

    private lateinit var settingsEntity: SettingsEntity

    private fun showPermanentNotification(
        config: NextPrayerConfig,
        pendingIntent: PendingIntent
    ) {
        val notificationChannel = buildNotificationChannel(
            isAlarmTime = false,
            channelName = NOTIFICATION_CHANNEL_PERMANENT_NAME,
            channelID = NOTIFICATION_CHANNEL_PERMANENT_ID
        )

        val notification =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_PERMANENT_ID)
                .setSmallIcon(R.drawable.ic_haya_notification)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(buildPermanentNotificationBannerInfo(config))
                .setContentIntent(pendingIntent)
                .setShowWhen(false)
                .build()
    }

    @SuppressLint("MissingPermission")
    private fun showAlarmNotification(config: NextPrayerConfig, pendingIntent: PendingIntent) {
        Timer().schedule(NOTIFICATION_UPDATE_SHORT) {
            val notification = buildNotification(
                pendingIntent = pendingIntent,
                notificationType = settingsEntity.notificationType,
                config = config
            )
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(Random.nextInt(0, Int.MAX_VALUE), notification)
            }
        }
    }

    private fun buildAlarmNotificationBannerInfo(config: NextPrayerConfig): RemoteViews {
        val notificationView =
            RemoteViews(
                applicationContext.packageName,
                R.layout.notification_service_alarm_remote_view
            )
        notificationView.setTextViewText(
            R.id.notification_alarm_next_prayer_text,
            config.nextPrayerName
        )
        return notificationView
    }

    private fun buildPermanentNotificationBannerInfo(config: NextPrayerConfig): RemoteViews {
        val notificationView =
            RemoteViews(
                applicationContext.packageName,
                R.layout.notification_service_permanent_remote_view
            )
        notificationView.setTextViewText(
            R.id.notification_permanent_next_prayer_banner_text,
            config.nextPrayerBanner
        )
        notificationView.setTextViewText(
            R.id.notification_permanent_next_prayer_text,
            config.nextPrayerName
        )
        notificationView.setTextViewText(
            R.id.notification_permanent_next_prayer_time,
            config.nextPrayerTime
        )
        return notificationView
    }

    private fun buildNotification(
        pendingIntent: PendingIntent,
        config: NextPrayerConfig,
        notificationType: String
    ) =
        when (notificationType) {
            NotificationType.SILENT.value -> createNotification(
                pendingIntent = pendingIntent,
                channelID = NOTIFICATION_CHANNEL_SILENT_ID,
                soundUri = Uri.EMPTY,
                config = config
            )

            NotificationType.TONE.value -> createNotification(
                pendingIntent = pendingIntent,
                channelID = NOTIFICATION_CHANNEL_TONE_ID,
                soundUri = defaultSystemRingtone,
                config = config
            )

            NotificationType.HALF.value -> createNotification(
                pendingIntent = pendingIntent,
                channelID = NOTIFICATION_CHANNEL_HALF_ID,
                soundUri = tools.getSoundUri(NotificationType.HALF),
                config = config
            )

            NotificationType.FULL.value -> createNotification(
                pendingIntent = pendingIntent,
                channelID = NOTIFICATION_CHANNEL_FULL_ID,
                soundUri = tools.getSoundUri(NotificationType.FULL),
                config = config
            )

            else -> createNotification(
                pendingIntent = pendingIntent,
                channelID = NOTIFICATION_CHANNEL_SILENT_ID,
                soundUri = Uri.EMPTY,
                config = config
            )
        }

    private fun createNotification(
        pendingIntent: PendingIntent,
        config: NextPrayerConfig,
        channelID: String,
        soundUri: Uri,
    ): Notification {
        val notificationChannel = buildNotificationChannel(
            isAlarmTime = true,
            channelName = NOTIFICATION_CHANNEL_ALARM_NAME,
            channelID = channelID
        )
        notificationChannel.setSound(
            soundUri,
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )
        return createNotificationWithSound(pendingIntent, config, channelID, soundUri)
    }

    private fun buildNotificationChannel(
        isAlarmTime: Boolean,
        channelName: String,
        channelID: String
    ): NotificationChannel =
        NotificationChannel(
            channelID,
            channelName,
            if (isAlarmTime) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW
        )

    private fun createNotificationWithSound(
        pendingIntent: PendingIntent,
        config: NextPrayerConfig,
        channelID: String,
        soundUri: Uri
    ): Notification =
        NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.ic_haya_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildAlarmNotificationBannerInfo(config))
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .setAutoCancel(true)
            .build()

    private fun createNotificationPendingIntent(): PendingIntent {
        val notificationIntent =
            Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

        return PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val defaultSystemRingtone =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    companion object {
        private const val NOTIFICATION_CHANNEL_PERMANENT_ID = "athan_notification_channel_permanent"
        private const val NOTIFICATION_CHANNEL_SILENT_ID = "athan_notification_channel_silent"
        private const val NOTIFICATION_CHANNEL_TONE_ID = "athan_notification_channel_tone"
        private const val NOTIFICATION_CHANNEL_HALF_ID = "athan_notification_channel_half"
        private const val NOTIFICATION_CHANNEL_FULL_ID = "athan_notification_channel_full"
        private const val NOTIFICATION_CHANNEL_ALARM_NAME = "Athan Alarm"
        private const val NOTIFICATION_CHANNEL_PERMANENT_NAME = "Prayer time"
        private const val NOTIFICATION_UPDATE_SHORT: Long = 20000
    }
}