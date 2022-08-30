package com.gals.prayertimes.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toDomain
import com.gals.prayertimes.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationService : Service() {
    private val backgroundJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.Main + backgroundJob)
    private val loading = MutableLiveData<Boolean>()
    private lateinit var repository: Repository
    private lateinit var tools: UtilsManager
    private lateinit var prayer: DomainPrayer
    private lateinit var settings: Settings
    private lateinit var notification: Notification
    private lateinit var notificationView: RemoteViews

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        configure()
        loadPrayerData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationView =
            RemoteViews(
                applicationContext.packageName,
                R.layout.notification_service_remote_view
            )
        return START_STICKY
    }

    private fun showNotification() {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_haya_notification)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationView)
                .setContentIntent(pendingIntent)
                .build()
        /**
         * .setSound(Uri)
         * */

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_SETTINGS,
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    private fun configure() {
        tools = UtilsManager(applicationContext)
        repository = Repository(
            database = AppDB.getInstance(applicationContext)
        )
    }

    private fun loadPrayerData() {
        backgroundScope.launch {
            loading.postValue(true)
            withContext(Dispatchers.IO) {
                prayer = repository.getPrayerFromLocalDataSource(getTodayDate())!!.toDomain()
                settings = repository.getSettingsFromLocalDataSource()!!
            }
            withContext(Dispatchers.Main) {
                if (prayer != null && prayer != DomainPrayer.EMPTY) {
                    loading.value = false
                    notificationView.setTextViewText(
                        R.id.notification_remaining_time,
                        prayer.isha
                    )
                    showNotification()
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "athan_notification_channel"
        private const val NOTIFICATION_CHANNEL_SETTINGS = "Athan Notification"
    }
}