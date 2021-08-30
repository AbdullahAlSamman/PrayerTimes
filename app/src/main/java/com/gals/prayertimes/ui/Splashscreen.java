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

    ToolsManager tools;
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
        tools = new ToolsManager(getBaseContext());

        // Change Statusbar color
        tools.changeStatusBarColor(getWindow());

        DataManager task = new DataManager(toMain, null, null, this.getBaseContext(), false);
        task.execute("", "", "");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            prayer.setPrayersAlarms();
            Log.i("ALARM", "From Splash Screen");
        }*/

    }
}
