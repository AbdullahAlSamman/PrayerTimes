package com.gals.prayertimes.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.utils.UtilsManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayersODNotificationService : Service() {
    var prayer: EntityPrayer? = null
    var tools: UtilsManager? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(
            "ODN/Service:",
            "Started"
        )
        tools = UtilsManager(baseContext)
        GetPrayerFromDB().execute(baseContext)
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        /**START_STICKY is for preventing service from being killed*/
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val pService = Intent(
                this,
                PrayersODNotificationServiceRestart::class.java
            )
            sendBroadcast(pService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i(
            "Service",
            "Service is destroyed"
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        try {
            val restartService = Intent(
                applicationContext,
                PrayersODNotificationServiceRestart::class.java
            )
            restartService.setPackage(packageName)
            val restartServicePI = PendingIntent.getService(
                applicationContext,
                1,
                restartService,
                PendingIntent.FLAG_ONE_SHOT
            )
            val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] = restartServicePI
            Log.i(
                "Service",
                "Service Task was removed"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class GetPrayerFromDB : AsyncTask<Context?, String?, String>() {
        override fun doInBackground(vararg params: Context?): String {
            prayer = getInstance(baseContext).prayerDao.findByDate(
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.US
                ).format(Date())
            )
            return ""
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            // TODO: fix the service to give notification with data from db
        }
    }
}