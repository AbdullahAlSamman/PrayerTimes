package com.gals.prayertimes.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.MeasureUnit.EM
import android.util.Log
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.db.entities.Prayer.Companion.EMPTY
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.utils.UtilsManager

class PrayersODNotificationServiceRestart : BroadcastReceiver() {
    lateinit var tools: UtilsManager
    lateinit var prayer: DomainPrayer
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        try {
            tools = UtilsManager(context)
            prayer = DomainPrayer.EMPTY

            /**This Receiver is for restarting the service for old devices*/
            /*TODO: delete this won't be needed*/
            val pService = Intent(
                context,
                PrayersODNotificationService::class.java
            )
            context.applicationContext.startService(pService)
            Log.i(
                "Service/Restart",
                "Receiver Started new Service"
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