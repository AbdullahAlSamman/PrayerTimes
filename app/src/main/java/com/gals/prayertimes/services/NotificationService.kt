package com.gals.prayertimes.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.gals.prayertimes.R
import com.gals.prayertimes.view.MainActivity

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_NOT_STICKY
    }

    private fun showNotification() {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_Id))
                .setContentTitle("Synchronization service")
                .setContentText("Downloading image")
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                getString(R.string.notification_channel_Id),
                getString(R.string.notification_channel_settings),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}