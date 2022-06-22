package com.gals.prayertimes.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gals.prayertimes.utils.DataManager;
import com.gals.prayertimes.utils.UtilsManager;


public class Splashscreen extends AppCompatActivity {

    UtilsManager tools;
    Intent       toMain;
    //TODO: change to database.
    //TODO: handle exceptions well.
    //TODO: Testing.
    //TODO: remove old code.
    //TODO: background services.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gals.prayertimes.R.layout.activity_splashscreen);

        toMain = new Intent(this, MainActivity.class);
        tools = new UtilsManager(getBaseContext());

        // Change Statusbar color
        tools.changeStatusBarColor(getWindow());

        new DataManager(toMain,
                        this.getBaseContext(), false).execute("", "", "");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            prayer.setPrayersAlarms();
            Log.i("ALARM", "From Splash Screen");
        }*/

    }
}
