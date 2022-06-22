package com.gals.prayertimes;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.gals.prayertimes.db.AppDB;
import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.services.PrayersODNotificationService;
import com.gals.prayertimes.utils.UtilsManager;

public class App extends Application {
    UtilsManager tools;
    Prayer       prayer;
    AppDB db;

    @Override
    public void onCreate() {
        super.onCreate();
        tools = new UtilsManager(getApplicationContext());
        db = AppDB.getInstance(getApplicationContext());

        try {
            Intent pService = new Intent(this, PrayersODNotificationService.class);
            startService(pService);
            Log.i("App/Service", "Starting new Service");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
