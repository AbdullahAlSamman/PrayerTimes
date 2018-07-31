package com.gals.prayertimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

@Deprecated
public class AlarmReceiver extends BroadcastReceiver {

    public static final String PREFS_NAME = "MyLocalStorage";
    Prayer prayer;
    ToolsManager tools;


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        tools = new ToolsManager(context);

        prayer = new Prayer(settings, context);
        prayer.getLocalStorage();
        Log.i("ALARM", "TimeCheck");
 /*       if (prayer.notficationCheckPrayers()) {
            if (prayer.getCurrentPrayerName() == context.getString(R.string.pSunrise)) {
                tools.createNotification(context.getString(R.string.notSunrise));
            } else
                tools.createNotification(context.getString(R.string.notPrayer) + " " + prayer.getCurrentPrayerName());

        }*/
//check if exist
//        boolean isWorking = (PendingIntent.getBroadcast(getActivity(), 1001, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }
}