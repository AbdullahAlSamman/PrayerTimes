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
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.utils.UtilsManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class PrayersODNotificationService : Service() {
    var prayerTimeCheckTask: TimerTask? = null
    var prayerTimeCheck: Timer? = null
    var prayer: Prayer? = null
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
        //START_STICKY is for preventing service from being killed
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Restarting the service from the Broadcast receiver API 17
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        try {
            val pService = Intent(
                this,
                PrayersODNotificationServiceRestart::class.java
            )
            sendBroadcast(pService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //        }
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
            prayer = getInstance(baseContext).prayerDao()!!.findByDate(
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.US
                ).format(Date())
            )
            return ""
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            //            TODO: fix the service to give notification with data from db
            //startForeground(12, new Notification());
            /*prayerTimeCheck = new Timer();
            prayerTimeCheckTask = new TimerTask() {
                @Override
                public void run() {
                   try {
                        if (prayer != null) {
                            if (prayer.getNotificaiton()) {
                                Log.i("Service", "TimeCheck");
                                if (prayer.notificationCheckPrayers()) {
                                    if (prayer.getCurrentPrayerName() == getString(R.string.pSunrise)) {
//                                if (prayer.getAthan() == null)
                                        tools.createNotification(getString(R.string.notSunrise), tools.generateRandomNotificationId());
//                                else
//                                    tools.createNotification(getString(R.string.notSunrise), prayer.silentAthan, Notification.DEFAULT_LIGHTS);
                                    } else {
                                        if (prayer.getAthan() == null)
                                            tools.createNotification(getString(R.string.notPrayer) + " " + prayer.getCurrentPrayerName(), tools.generateRandomNotificationId());
                                        else
                                            tools.createNotification(getString(R.string.notPrayer) + " " + prayer.getCurrentPrayerName(), prayer.getAthan(), Notification.DEFAULT_LIGHTS, tools.generateRandomNotificationId());
                                    }
                                    Log.i("Service", "Notificaiton for " + prayer.getCurrentPrayerName());
                                    // Sleep for 1 Min so only one notification works
                                    //TimeUnit.MINUTES.sleep(1);

                                    Thread.sleep(60000);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            prayerTimeCheck.scheduleAtFixedRate(prayerTimeCheckTask, 1000, 20000);
        }*/
        }
    }
}