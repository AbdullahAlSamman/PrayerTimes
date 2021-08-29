package com.gals.prayertimes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.gals.prayertimes.utils.DataManager;
import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.R;
import com.gals.prayertimes.utils.ToolsManager;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyLocalStorage";
    static boolean needUpdate = false;
    Intent main;
    ImageView background;
    Prayer prayer;
    LinearLayout fajerLay;
    LinearLayout sunriseLay;
    LinearLayout duhrLay;
    LinearLayout asrLay;
    LinearLayout maghribLay;
    LinearLayout ishaLay;
    TextView fajerText;
    TextView sunriseText;
    TextView duhrText;
    TextView asrText;
    TextView maghribText;
    TextView ishaText;
    TextView sDatePanner;
    TextView mDatePanner;
    TextView dayPanner;
    TextView fajerTime;
    TextView sunriseTime;
    TextView duhrTime;
    TextView asrTime;
    TextView maghribTime;
    TextView ishaTime;
    TextView nextPrayerTime;
    TextView nextPrayerText;
    TextView nextPrayerPanner;
    ImageButton btnSettings;
    ToolsManager tools;
    SharedPreferences settings;
    Timer updateUITimer;
    private com.facebook.ads.AdView adView;
    private AdSettings adSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gals.prayertimes.R.layout.activity_main);

        //Tools and Local Storage
        settings = getSharedPreferences(PREFS_NAME, 0);
        tools = new ToolsManager(getBaseContext());

        //UI Elements
        background = (ImageView) findViewById(com.gals.prayertimes.R.id.background);
        sDatePanner = (TextView) findViewById(com.gals.prayertimes.R.id.sDateView);
        mDatePanner = (TextView) findViewById(com.gals.prayertimes.R.id.mDateView);
        dayPanner = (TextView) findViewById(com.gals.prayertimes.R.id.dayView);
        fajerTime = (TextView) findViewById(com.gals.prayertimes.R.id.fajerTimeText);
        sunriseTime = (TextView) findViewById(com.gals.prayertimes.R.id.sunriseTimeText);
        duhrTime = (TextView) findViewById(com.gals.prayertimes.R.id.duhrTimeText);
        asrTime = (TextView) findViewById(com.gals.prayertimes.R.id.asrTimeText);
        maghribTime = (TextView) findViewById(com.gals.prayertimes.R.id.maghribTimeText);
        ishaTime = (TextView) findViewById(com.gals.prayertimes.R.id.ishaTimeText);
        nextPrayerText = (TextView) findViewById(com.gals.prayertimes.R.id.nextPrayerText);
        nextPrayerTime = (TextView) findViewById(com.gals.prayertimes.R.id.nextTimeText);
        nextPrayerPanner = (TextView) findViewById(R.id.prayerText);

        fajerText = (TextView) findViewById(R.id.fajertext);
        sunriseText = (TextView) findViewById(R.id.sunriseText);
        duhrText = (TextView) findViewById(R.id.duhrText);
        asrText = (TextView) findViewById(R.id.asrText);
        maghribText = (TextView) findViewById(R.id.maghribText);
        ishaText = (TextView) findViewById(R.id.ishaText);

        fajerLay = (LinearLayout) findViewById(R.id.fajerLayout);
        sunriseLay = (LinearLayout) findViewById(R.id.sunriseLayout);
        duhrLay = (LinearLayout) findViewById(R.id.duhrLayout);
        asrLay = (LinearLayout) findViewById(R.id.asrLayout);
        maghribLay = (LinearLayout) findViewById(R.id.maghribLayout);
        ishaLay = (LinearLayout) findViewById(R.id.ishaLayout);

        btnSettings = (ImageButton) findViewById(R.id.settingButton);


        //Ads Facebook Code
        adView = new com.facebook.ads.AdView(this, "215308835976224_215309109309530", AdSize.BANNER_HEIGHT_90);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.i("UI/Ads", "Error: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // Request an ad
        adView.loadAd();


        // Change Status bar color
        tools.changeStatusBarColor(getWindow());

        prayer = new Prayer(settings, this.getBaseContext());

        try {
            //TODO: Internet warning
            main = getIntent();
            if (main.getStringExtra("checkInternt") != null) {
//                make an massage internet connection
                Log.e("info", main.getStringExtra("checkInternt"));
                setSettingsUI();
            } else if (main.getStringExtra("result") != null) {
                Log.e("info", "There is no Results from database please check");
                setSettingsUI();
            } else {
                Log.e("info", "internet Connection is good");
                setSettingsUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        startUIUpdate(20000);

        changeFontSizes();
        tools.setActivityLanguage("en");
        final Intent settingsActivity = new Intent(this, Menu.class);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(settingsActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSettingsUI();
    }

    @Override
    protected void onDestroy() {
        //Ads get Destroyed
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Update Background pic according to time
    private void updateBackground() {
        if (!prayer.isNight()) {
            if (prayer.isRamadan()) {
                background.setImageResource(R.drawable.ramadan_day);
            } else
                background.setImageResource(R.drawable.background_day);
            btnSettings.setImageResource(R.drawable.round_settings_black);
            Log.i("UI", "Day time");
        } else {
            if (prayer.isRamadan()) {
                background.setImageResource(R.drawable.ramadan_night);
            } else
                background.setImageResource(R.drawable.background_night);
            btnSettings.setImageResource(R.drawable.round_settings_white);
            Log.i("UI", "Night time");
        }
    }

    //update the time to next prayer
    private void updateRemaningTime() {
        if (prayer.calculateTimeBetweenPrayers()) {

            if (prayer.isNextAPrayer()) {
                nextPrayerTime.setText(prayer.getRemmainingPrayerTime());
                nextPrayerText.setText(prayer.getCurrentPrayerName());
                nextPrayerPanner.setText(getString(R.string.remmaning_prayer_time));
            } else {
                nextPrayerTime.setText(prayer.getRemmainingPrayerTime());
                nextPrayerText.setText(prayer.getCurrentPrayerName());
                nextPrayerPanner.setText(getString(R.string.remmaning_time));
            }

            nextPrayerTime.setVisibility(View.VISIBLE);
            nextPrayerText.setVisibility(View.VISIBLE);
            nextPrayerPanner.setVisibility(View.VISIBLE);

        } else {
            nextPrayerTime.setVisibility(View.INVISIBLE);
            nextPrayerText.setVisibility(View.INVISIBLE);
            nextPrayerPanner.setVisibility(View.INVISIBLE);
        }
    }

    //Set data on the UI Elements
    private void setSettingsUI() {
        try {
            if (prayer.getLocalStorage()) {
//            prayer.printTest();
                prayer.dateText();

                Log.i("isChangeTheDayTest", "" + prayer.isDayHasChanged(prayer.getSDate()));

                if (prayer.isDayHasChanged(prayer.getSDate())) {
                    updatePrayers(this.settings);
                }

                updateRemaningTime();
                updateBackground();

                sDatePanner.setText(prayer.getSFullDate());
                mDatePanner.setText(prayer.getMFullDate());
                dayPanner.setText(prayer.getDay());
                fajerTime.setText(prayer.getFajer());
                sunriseTime.setText(prayer.getSunrise());
                duhrTime.setText(prayer.getDuhr());
                asrTime.setText(prayer.getAsr());
                maghribTime.setText(prayer.getMaghrib());
                ishaTime.setText(prayer.getIsha());
            } else {
                Log.i("SettingsUI Update", "there is no data at all");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updatePrayers(SharedPreferences settings) {
        // update the data from server if the date is changed
        if (tools.isNetworkAvailable()) {
            try {
                DataManager task = new DataManager(null, prayer, settings, this.getBaseContext(), true);
                task.execute("", "", "");
                prayer.getLocalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Fonts for small devices and change the size of Squares
    private void changeFontSizes() {
        if (tools.isScreenSmall()) {
            sDatePanner.setTextSize(16);
            mDatePanner.setTextSize(16);
            dayPanner.setTextSize(16);

            nextPrayerPanner.setTextSize(18);
            nextPrayerText.setTextSize(18);
            nextPrayerTime.setTextSize(20);

            fajerText.setTextSize(16);
            sunriseText.setTextSize(16);
            duhrText.setTextSize(16);
            asrText.setTextSize(16);
            maghribText.setTextSize(16);
            ishaText.setTextSize(16);

            fajerTime.setTextSize(16);
            sunriseTime.setTextSize(16);
            duhrTime.setTextSize(16);
            asrTime.setTextSize(16);
            maghribTime.setTextSize(16);
            ishaTime.setTextSize(16);

            LinearLayout.LayoutParams heightParams = (LinearLayout.LayoutParams) fajerLay.getLayoutParams();
            heightParams.height = 150;
            fajerLay.setLayoutParams(heightParams);
            sunriseLay.setLayoutParams(heightParams);
            duhrLay.setLayoutParams(heightParams);
            asrLay.setLayoutParams(heightParams);
            maghribLay.setLayoutParams(heightParams);
            ishaLay.setLayoutParams(heightParams);
        }
    }

    // Timer to update the ui
    private void startUIUpdate(int time) {
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            setSettingsUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        updateUITimer = new Timer();
        updateUITimer.schedule(timertask, 0, time);
    }
}


