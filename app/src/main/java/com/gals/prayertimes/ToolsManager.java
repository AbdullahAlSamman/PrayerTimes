package com.gals.prayertimes;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Genius on 13.02.2018.
 */

public class ToolsManager {

    private Context toolscontext;
    private NotificationManager notifManager;
    private long[] viberationPattern = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400};

    public ToolsManager(Context context) {
        toolscontext = context;
    }

    public static String convert2Decimal(String number) {
        try {
            char[] chars = new char[number.length()];
            for (int i = 0; i < number.length(); i++) {
                char ch = number.charAt(i);
                if (ch >= 0x0660 && ch <= 0x0669)
                    ch -= 0x0660 - '0';
                else if (ch >= 0x06f0 && ch <= 0x06F9)
                    ch -= 0x06f0 - '0';
                chars[i] = ch;
            }
            return new String(chars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) toolscontext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String convertDate() {
        String todayDay = new SimpleDateFormat("dd").format(new Date());
        String todayMonth = new SimpleDateFormat("MM").format(new Date());
        String todayYear = new SimpleDateFormat("yyyy").format(new Date());
        return convert2Decimal(todayDay) + "." + convert2Decimal(todayMonth) + "." + convert2Decimal(todayYear);
    }

    public Date convertStringToDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNotification(String aMessage, Uri sound, int notificaitonDefaults, int notificationID) {
        // There are hardcoding only for show it's just strings
        String name = toolscontext.getString(R.string.notificationChannelSettings);
        String id = toolscontext.getString(R.string.notificationChannelId); // The user-visible name of the channel.
        String description = toolscontext.getString(R.string.notificationsChannelDescription); // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager) toolscontext.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH; //Makes Sound and Headup Notification
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);

            // Notification Attributes for Oreo+
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                mChannel.setSound(sound, attributes); // Sound set must be on the Notification Channel not the Builder
                mChannel.setVibrationPattern(viberationPattern);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(toolscontext, id);

            intent = new Intent(toolscontext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(toolscontext, 0, intent, 0);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(R.drawable.ic_haya_notification) // required
                    .setColor(toolscontext.getColor(R.color.fajerViewColor))
                    .setContentText("")  // required
//                    .setDefaults(notificaitonDefaults) //Don't use Defaults that will block the channel settings
                    .setAutoCancel(true)
                    .setTicker(aMessage)
                    .setContentIntent(pendingIntent);

        } else {

            builder = new NotificationCompat.Builder(toolscontext);
            intent = new Intent(toolscontext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(toolscontext, 0, intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_haya_notification);
            } else
                builder.setSmallIcon(R.mipmap.ic_notification);

            builder.setContentTitle(aMessage)                           // required
                    .setContentText(toolscontext.getString(R.string.app_name))  // required
                    .setDefaults(notificaitonDefaults)
                    .setAutoCancel(true)
                    .setTicker(aMessage)
                    .setVibrate(viberationPattern)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(notificationID, notification);
    }

    public void createNotification(String aMessage, int notificationID) {

        // There are hardcoding only for show it's just strings
        String name = toolscontext.getString(R.string.sunriseNotificationChannelSettings);
        String id = toolscontext.getString(R.string.sunriseNotificationChannelId); // The user-visible name of the channel.
        String description = toolscontext.getString(R.string.sunriseNotificationsChannelDescription); // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager) toolscontext.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                mChannel.setVibrationPattern(viberationPattern);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(toolscontext, id);

            intent = new Intent(toolscontext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(toolscontext, 0, intent, 0);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(R.drawable.ic_haya_notification) // required
                    .setColor(toolscontext.getColor(R.color.fajerViewColor))
                    .setContentText("")  // required
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage);

        } else {

            builder = new NotificationCompat.Builder(toolscontext);

            intent = new Intent(toolscontext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(toolscontext, 0, intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_haya_notification);
            } else
                builder.setSmallIcon(R.mipmap.ic_notification);

            builder.setContentTitle(aMessage)                           // required
                    .setContentText(toolscontext.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(viberationPattern)
                    .setPriority(Notification.PRIORITY_HIGH);

        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(notificationID, notification);
    }

    public void setActivityLanguage(String lang) {
        Resources res = toolscontext.getResources();
        Configuration newConfig = new Configuration(res.getConfiguration());
        Locale locale = new Locale(lang);
        newConfig.locale = locale;
        newConfig.setLayoutDirection(locale);
        res.updateConfiguration(newConfig, null);
    }

    public Boolean isEqualTime(Calendar currentTime, Calendar nextTime) {

        int bigHours = (int) Math.ceil((currentTime.getTimeInMillis() / (1000 * 60 * 60)));
        int bigMins = (int) Math.ceil((currentTime.getTimeInMillis() / (1000 * 60)) % 60);
        int smallHours = (int) Math.ceil((nextTime.getTimeInMillis() / (1000 * 60 * 60)));
        int smallMins = (int) Math.ceil((nextTime.getTimeInMillis() / (1000 * 60)) % 60);

        if (bigHours == smallHours) {
            if (bigMins == smallMins) {
                return true;
            }
        }
        return false;
    }

    public String difTimes(Calendar big, Calendar small) {
        Long result = big.getTimeInMillis() - small.getTimeInMillis();
        int Hours = (int) Math.ceil(result / (1000 * 60 * 60));
        int Mins = (int) Math.ceil((result / (1000 * 60)) % 60);
        String mins = Mins + "";
        String hours = Hours + "";

        if (Mins < 10) {
            mins = "0" + Mins;
        }
        if (Hours < 10) {
            hours = "0" + Hours;
        }

        return hours + ":" + mins;
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) toolscontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public Boolean isRTL() {
        Configuration config = toolscontext.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            config.setLayoutDirection(new Locale("ar"));
            toolscontext.getResources().updateConfiguration(config, toolscontext.getResources().getDisplayMetrics());
            //in Right To Left layout
            Log.i("RTL", "The Language is RTL");
            return true;
        } else {
            Log.i("RTL", "The Language is LTR");
            return false;
        }
    }

    public void changeStatusBarColor(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(toolscontext.getResources().getColor(R.color.ishaViewColor));
        }
    }

    public Boolean isScreenSmall() {
        double density = toolscontext.getResources().getDisplayMetrics().density;
        if (density <= 1.5) {
            return true;
        }
        return false;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(Class<?> serviceClass) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {

                ComponentName serviceComponent = new ComponentName(toolscontext, serviceClass.getName());

                JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    builder.setPeriodic(20 * 1000);
                    builder.setPersisted(true);
                } else {
                    builder.setMinimumLatency(20 * 1000); // wait at least
                    builder.setOverrideDeadline(30 * 1000); // maximum delay
                }
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
                builder.setRequiresDeviceIdle(false); // device should be idle
                builder.setRequiresCharging(false); // we don't care if the device is charging or not


                JobScheduler jobScheduler = (JobScheduler) toolscontext.getSystemService(Context.JOB_SCHEDULER_SERVICE);

                jobScheduler.schedule(builder.build());
                Log.i("JobScheduler", "Scheduled job " + serviceClass.getName() + " started");

            } catch (Exception e) {
                Log.i("JobScheduler", "Failed to schedule the job");
                e.printStackTrace();
            }
        }
    }

    public int generateRandomNotificationId() {
        Random rand = new Random();

        return rand.nextInt(5000 - 1000 + 1) + 1000;
    }

    public void restartActivity(Context currentActivity, final Class<? extends Activity> activityToStart) {
        Intent intent = new Intent(currentActivity, activityToStart);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(intent);
    }
}
