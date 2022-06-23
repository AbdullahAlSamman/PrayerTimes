package com.gals.prayertimes.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.utils.UtilsManager

class PrayersODNotificationServiceRestart : BroadcastReceiver() {
    lateinit var tools: UtilsManager
    lateinit var prayer: Prayer
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        try {
            tools = UtilsManager(context)
            prayer = Prayer()
            prayer.activity = context

            //This Receiver is for restarting the service for old devices
            if (prayer.notification) {
                val pService = Intent(
                    context,
                    PrayersODNotificationService::class.java
                )
                context.applicationContext.startService(pService)
                Log.i(
                    "Service/Restart",
                    "Receiver Started new Service"
                )
            } else Log.i(
                "Service/Restart",
                "has not restarted Notification is off"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(
                "Service/Restart",
                "failed to Restart the service"
            )
        }
    }
}