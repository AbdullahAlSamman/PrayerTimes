package com.gals.prayertimes;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Menu extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {

    public static final String PREFS_NAME = "MyLocalStorage";
    ToolsManager tools;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tools = new ToolsManager(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.mysalay);

        SettingsFragment athanSettings = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, athanSettings).commit();

        tools.setActivityLanguage("en");
        tools.changeStatusBarColor(getWindow());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Back to main UI
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
