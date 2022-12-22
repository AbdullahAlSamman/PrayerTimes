package com.gals.prayertimes.view

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.gals.prayertimes.R
import com.gals.prayertimes.adapter.SettingsMenuContent.SettingsMenuItem
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.view.MenuItemFragment.OnListFragmentInteractionListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Menu : AppCompatActivity(), AthanSettingsFragment.OnFragmentInteractionListener,
    OnListFragmentInteractionListener, PrivacyPolicyFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var tools: UtilsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setIcon(R.drawable.mysalay)
        val menuSettings = MenuItemFragment()

        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            menuSettings
        ).commit()

        tools.setActivityLanguage("en")
        tools.changeStatusBarColor(window)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**Back to main UI*/
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {}
        }
        return true
    }

    override fun onFragmentInteraction(uri: Uri?) {}
    override fun onListFragmentInteraction(item: SettingsMenuItem?) {}
}