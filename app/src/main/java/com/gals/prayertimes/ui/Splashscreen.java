package com.gals.prayertimes.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gals.prayertimes.utils.DataManager;
import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.db.AppDB;
import com.gals.prayertimes.utils.ToolsManager;


public class Splashscreen extends AppCompatActivity {
    public static final String PREFS_NAME = "MyLocalStorage";
    Prayer prayer;
    SharedPreferences settings;
    ToolsManager tools;
    AppDB db;
    Intent toMain;
    //TODO: change to database.
    //TODO: handel exceptions well.
    //TODO: Testing.
    //TODO: remove old code.
    //TODO: background services.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gals.prayertimes.R.layout.activity_splashscreen);

        toMain = new Intent(this, MainActivity.class);
        settings = getSharedPreferences(PREFS_NAME, 0);
        prayer = new Prayer(settings, this.getBaseContext());
        tools = new ToolsManager(getBaseContext());

        // Change Statusbar color
        tools.changeStatusBarColor(getWindow());

        DataManager task = new DataManager(toMain, prayer, settings, this.getBaseContext(), false);
        task.execute("", "", "");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            prayer.setPrayersAlarms();
            Log.i("ALARM", "From Splash Screen");
        }*/

    }
}
