package com.gals.prayertimes.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.gals.prayertimes.R
import com.gals.prayertimes.view.MainActivity

class NotificationService : Service() {
    private lateinit var notification: Notification
    private lateinit var notificationView: RemoteViews

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationView =
            RemoteViews(applicationContext.packageName, R.layout.notification_forground_service)
        showNotification()
        return START_NOT_STICKY
    }

    private fun showNotification() {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_haya_notification)
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
            NotificationManager.IMPORTANCE_NONE
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "athan_notification_channel"
        private const val NOTIFICATION_CHANNEL_SETTINGS = "Athan Notification"
    }
}