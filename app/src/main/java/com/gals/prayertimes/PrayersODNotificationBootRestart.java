package com.gals.prayertimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class PrayersODNotificationBootRestart extends BroadcastReceiver {
    public static final String PREFS_NAME = "MyLocalStorage";
    ToolsManager tools;
    Prayer prayer;
    SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            tools = new ToolsManager(context);
            settings = context.getSharedPreferences(PREFS_NAME, 0);
            prayer = new Prayer(settings, context);

            //This Receiver is for restarting the service for old devices
            if (prayer.getNotificaiton()) {
                Intent pService = new Intent(context, PrayersODNotificationService.class);
                context.getApplicationContext().startService(pService);
                Log.i("Service", "Receiver Started new Service");
            } else
                Log.i("Service", "has not restarted Notification is off");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Service", "Boot Complete failed to Restart the service");
        }
    }

}
