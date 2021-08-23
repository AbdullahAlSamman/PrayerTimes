package com.gals.prayertimes;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

// in order to get the Fragments to work you need to implement those Interactions interfaces it is necessary.
public class Menu extends AppCompatActivity implements AthanSettingsFragment.OnFragmentInteractionListener, MenuItemFragment.OnListFragmentInteractionListener, PrivacyPolicyFragment.OnFragmentInteractionListener {

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

        MenuItemFragment menuSettings = new MenuItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, menuSettings).commit();

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


    @Override
    public void onListFragmentInteraction(SettingsMenuContent.SettingsMenuItem item) {

    }
}
