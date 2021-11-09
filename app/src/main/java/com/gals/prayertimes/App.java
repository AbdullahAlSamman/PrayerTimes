package com.gals.prayertimes;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gals.prayertimes.db.AppDB;
import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.services.PrayersODNotificationService;
import com.gals.prayertimes.utils.ToolsManager;

import java.util.Locale;

/**
 * Created by Genius on 1/25/2018.
 */

public class App extends Application {
    ToolsManager tools;
    Prayer prayer;
    AppDB db;

    @Override
    public void onCreate() {
        super.onCreate();
        tools = new ToolsManager(getApplicationContext());
        db = AppDB.getInstance(getApplicationContext());

        try {
//            if (prayer.getNotificaiton()) {
            Intent pService = new Intent(this, PrayersODNotificationService.class);
            startService(pService);
            Log.i("App/Service", "Starting new Service");
//            } else
//                Log.i("App/Service", "not starting Notification is off");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
