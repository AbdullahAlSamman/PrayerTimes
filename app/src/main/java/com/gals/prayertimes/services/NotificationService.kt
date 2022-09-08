package com.gals.prayertimes.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.config.NextPrayerInfoConfig
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toDomain
import com.gals.prayertimes.utils.toTimePrayer
import com.gals.prayertimes.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationService : Service() {
    private val backgroundJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.Main + backgroundJob)
    private val loading = MutableLiveData<Boolean>()
    private lateinit var repository: Repository
    private lateinit var calculation: PrayerCalculation
    private lateinit var timerHandler: Handler
    private lateinit var tools: UtilsManager
    private lateinit var prayer: DomainPrayer
    private lateinit var settings: Settings

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        configure()
        loadPrayerData()
        timerHandler = Handler(Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun showNotification(config: NextPrayerInfoConfig, isAlarmTime: Boolean = false) {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val notification = buildNotification(
            pendingIntent = pendingIntent,
            notificationType = settings.notificationType,
            config = config,
            isAlarmTime = isAlarmTime
        )
        startForeground(1, notification)
    }

    private fun configure() {
        tools = UtilsManager(applicationContext)
        repository = Repository(
            database = AppDB.getInstance(applicationContext)
        )
        calculation = PrayerCalculation(applicationContext)
    }

    private fun buildNotificationChannel(notificationChannel: NotificationChannel) {
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(notificationChannel)
    }

    private fun loadPrayerData() {
        backgroundScope.launch {
            loading.postValue(true)
            withContext(Dispatchers.IO) {
                prayer = repository.getPrayerFromLocalDataSource(getTodayDate())!!.toDomain()
                settings = repository.getSettingsFromLocalDataSource()!!
            }
            withContext(Dispatchers.Main) {
                if (prayer != DomainPrayer.EMPTY) {
                    startUpdates()
                    loading.value = false
                }
            }
        }
    }

    private fun startUpdates() {
        timerHandler.post(object : Runnable {
            override fun run() {
                val config = calculation.calculateNextPrayerInfo(prayer.toTimePrayer())
                val nextPrayer = calculation.calculateNextPrayer(prayer.toTimePrayer())
                val isAlarmTime = calculation.isNowEqualsTime(nextPrayer)
                Log.i("Alarm Time", isAlarmTime.toString())
                if (isAlarmTime) {
                    timerHandler.postDelayed(this, NOTIFICATION_UPDATE_LONG)
                    showNotification(
                        config = config,
                        isAlarmTime = isAlarmTime
                    )
                } else {
                    timerHandler.postDelayed(this, NOTIFICATION_UPDATE_SHORT)
                    showNotification(
                        config = config,
                        isAlarmTime = isAlarmTime
                    )
                }
            }
        })
    }

    private fun buildNotificationBannerInfo(config: NextPrayerInfoConfig): RemoteViews {
        val notificationView =
            RemoteViews(
                applicationContext.packageName,
                R.layout.notification_service_remote_view
            )
        notificationView.setTextViewText(
            R.id.notification_next_prayer_text,
            config.nextPrayerBannerText
        )
        notificationView.setTextViewText(
            R.id.notification_next_prayer_text,
            config.nextPrayerNameText
        )
        notificationView.setTextViewText(R.id.notification_next_prayer_time, config.nextPrayerTime)
        return notificationView
    }

    private fun buildNotification(
        pendingIntent: PendingIntent,
        config: NextPrayerInfoConfig,
        notificationType: String,
        isAlarmTime: Boolean
    ) =
        when (notificationType) {
            NotificationType.SILENT.value -> createNotification(
                pendingIntent = pendingIntent,
                isAlarmTime = isAlarmTime,
                channelID = NOTIFICATION_CHANNEL_SILENT_ID,
                soundUri = Uri.EMPTY,
                config = config
            )
            NotificationType.TONE.value -> createNotification(
                pendingIntent = pendingIntent,
                isAlarmTime = isAlarmTime,
                channelID = NOTIFICATION_CHANNEL_TONE_ID,
                soundUri = defaultSystemRingtone,
                config = config
            )
            NotificationType.HALF.value -> createNotification(
                pendingIntent = pendingIntent,
                isAlarmTime = isAlarmTime,
                channelID = NOTIFICATION_CHANNEL_HALF_ID,
                soundUri = tools.getSoundUri(NotificationType.HALF),
                config = config
            )
            NotificationType.FULL.value -> createNotification(
                pendingIntent = pendingIntent,
                isAlarmTime = isAlarmTime,
                channelID = NOTIFICATION_CHANNEL_FULL_ID,
                soundUri = tools.getSoundUri(NotificationType.FULL),
                config = config
            )
            else -> createNotification(
                pendingIntent = pendingIntent,
                isAlarmTime = isAlarmTime,
                channelID = NOTIFICATION_CHANNEL_SILENT_ID,
                soundUri = Uri.EMPTY,
                config = config
            )
        }

    private fun createNotification(
        pendingIntent: PendingIntent,
        config: NextPrayerInfoConfig,
        channelID: String,
        soundUri: Uri,
        isAlarmTime: Boolean
    ): Notification =
        if (!isAlarmTime) {
            buildNotificationChannel(buildNotificationChannel(isAlarmTime, channelID))
            createNotificationWithoutSound(pendingIntent, config, channelID)
        } else {
            val notificationChannel = buildNotificationChannel(isAlarmTime, channelID)
            notificationChannel.setSound(
                soundUri,
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            buildNotificationChannel(notificationChannel)
            createNotificationWithSound(pendingIntent, config, channelID, soundUri)
        }

    private fun buildNotificationChannel(
        isAlarmTime: Boolean,
        channelID: String
    ): NotificationChannel =
        NotificationChannel(
            channelID,
            NOTIFICATION_CHANNEL_NAME,
            if (isAlarmTime) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW
        )

    private fun createNotificationWithSound(
        pendingIntent: PendingIntent,
        config: NextPrayerInfoConfig,
        channelID: String,
        soundUri: Uri
    ): Notification =
        NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_haya_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildNotificationBannerInfo(config))
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .build()

    private fun createNotificationWithoutSound(
        pendingIntent: PendingIntent,
        config: NextPrayerInfoConfig,
        channelID: String
    ): Notification =
        NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_haya_notification)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(buildNotificationBannerInfo(config))
            .setContentIntent(pendingIntent)
            .setSound(Uri.EMPTY)
            .build()

    private val defaultSystemRingtone =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    companion object {
        private const val NOTIFICATION_CHANNEL_SILENT_ID = "athan_notification_channel_silent"
        private const val NOTIFICATION_CHANNEL_TONE_ID = "athan_notification_channel_tone"
        private const val NOTIFICATION_CHANNEL_HALF_ID = "athan_notification_channel_half"
        private const val NOTIFICATION_CHANNEL_FULL_ID = "athan_notification_channel_full"
        private const val NOTIFICATION_CHANNEL_NAME = "Athan Notification"
        private const val NOTIFICATION_UPDATE_LONG: Long = 90000
        private const val NOTIFICATION_UPDATE_SHORT: Long = 20000
    }
}