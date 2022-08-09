package com.gals.prayertimes.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class NotificationService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return NotificationServiceBinder()
    }

    inner class NotificationServiceBinder : Binder() {
        val service: NotificationService
            get() = this@NotificationService
    }
}