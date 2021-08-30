package com.gals.prayertimes.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.utils.ToolsManager;

public class PrayersODNotificationServiceRestart extends BroadcastReceiver {
    ToolsManager tools;
    Prayer prayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            tools = new ToolsManager(context);
            prayer = new Prayer(context);

            //This Receiver is for restarting the service for old devices
            if (prayer.getNotificaiton()) {
                Intent pService = new Intent(context, PrayersODNotificationService.class);
                context.getApplicationContext().startService(pService);
                Log.i("Service/Restart", "Receiver Started new Service");
            } else
                Log.i("Service/Restart", "has not restarted Notification is off");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Service/Restart", "failed to Restart the service");
        }
    }
}
