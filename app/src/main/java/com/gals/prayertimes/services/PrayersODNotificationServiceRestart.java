package com.gals.prayertimes.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.utils.UtilsManager;

public class PrayersODNotificationServiceRestart extends BroadcastReceiver {
    UtilsManager tools;
    Prayer       prayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            tools = new UtilsManager(context);
            prayer = new Prayer();
            prayer.setActivity(context);

            //This Receiver is for restarting the service for old devices
            if (prayer.getNotification()) {
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
