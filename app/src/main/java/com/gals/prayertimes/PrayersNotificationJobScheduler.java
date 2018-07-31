package com.gals.prayertimes;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by Genius on 02.03.2018.
 */
@Deprecated
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PrayersNotificationJobScheduler extends JobService {

    public static final String PREFS_NAME = "MyLocalStorage";
    Prayer prayer;
    ToolsManager tools;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("JobScheduler", "Triggered On start");
//        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            tools = new ToolsManager(getBaseContext());

            prayer = new Prayer(settings, this);
            prayer.getLocalStorage();

       /*     if (prayer.notficationCheckPrayers()) {
                if (prayer.getCurrentPrayerName() == getString(R.string.pSunrise)) {
                    tools.createNotification(getString(R.string.notSunrise));
                } else
                    tools.createNotification(getString(R.string.notPrayer) + " " + prayer.getCurrentPrayerName());
                Log.i("Service", "Notificaiton for " + prayer.getCurrentPrayerName());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("JobScheduler", "Triggered On Stop");
        return false;
    }
}
