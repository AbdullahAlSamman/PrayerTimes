package com.gals.prayertimes.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(NOTIFICATION_MESSAGE).orEmpty()
        val title = intent?.getStringExtra(NOTIFICATION_TITLE).orEmpty()
        // TODO: Show notification via notification manager
        Log.i("ngz_notification", "received title: $title, message: $message")
    }
}