package com.gals.prayertimes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PrayersODNotificationService extends Service {

    public static final String PREFS_NAME = "MyLocalStorage";
    TimerTask prayerTimeCheckTask;
    Timer prayerTimeCheck;
    Prayer prayer;
    ToolsManager tools;

    public PrayersODNotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service:", "Started");

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        tools = new ToolsManager(getBaseContext());

        prayer = new Prayer(settings, this);

        //startForeground(12, new Notification());
        //TODO: update the localStorage when is not today
        prayerTimeCheck = new Timer();
        prayerTimeCheckTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    prayer.getLocalStorage();
                    if (prayer.getNotificaiton()) {
                        Log.i("Service", "TimeCheck");
                        if (prayer.notficationCheckPrayers()) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        prayerTimeCheck.scheduleAtFixedRate(prayerTimeCheckTask, 1000, 20000);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //START_STICKY is for preventing service from being killed
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Restarting the service from the Broadcast receiver API 17
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        try {
            Intent pService = new Intent(this, PrayersODNotificationServiceRestart.class);
            sendBroadcast(pService);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
        Log.i("Service", "Service is destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //Restarting the service for API 19
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
        try {
            Intent restartService = new Intent(getApplicationContext(),
                    PrayersODNotificationServiceRestart.class);
            restartService.setPackage(getPackageName());
            PendingIntent restartServicePI = PendingIntent.getService(
                    getApplicationContext(), 1, restartService,
                    PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);

            Log.i("Service", "Service Task was removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }
}
