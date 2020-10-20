package com.gals.prayertimes;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Genius on 1/25/2018.
 */

public class App extends Application {

    public static final String PREFS_NAME = "MyLocalStorage";
    ToolsManager tools;
    Prayer prayer;
    SharedPreferences settings;

    @Override
    public void onCreate() {
        super.onCreate();
        tools = new ToolsManager(getApplicationContext());

        settings = getSharedPreferences(PREFS_NAME, 0);
        prayer = new Prayer(settings, getApplicationContext());
        try {
            if (prayer.getNotificaiton()) {
                Intent pService = new Intent(this, PrayersODNotificationService.class);
                startService(pService);
                Log.i("Service", "Starting new Service");
            } else
                Log.i("Service", "not starting Notification is off");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
