package com.gals.prayertimes

import android.app.Application
import android.content.Intent
import android.util.Log
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.services.PrayersODNotificationService
import com.gals.prayertimes.utils.UtilsManager

typealias DomainPrayer = Prayer
typealias EntityPrayer = com.gals.prayertimes.db.entities.Prayer

class App : Application() {
    lateinit var tools: UtilsManager
    lateinit var prayer: Prayer
    lateinit var db: AppDB
    override fun onCreate() {
        super.onCreate()
        tools = UtilsManager(applicationContext)
        db = getInstance(applicationContext)
        try {
            val pService = Intent(
                this,
                PrayersODNotificationService::class.java
            )
            startService(pService)
            Log.i(
                "App/Service",
                "Starting new Service"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}