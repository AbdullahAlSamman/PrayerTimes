package com.gals.prayertimes.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gals.prayertimes.navigation.PrayerTimesNavHost
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.utils.getStatusBarColors
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrayerTimesTheme {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val statusBarSettings = getStatusBarColors()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarSettings.first,
                        darkIcons = statusBarSettings.second
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PrayerTimesNavHost(navController = navController, modifier = Modifier)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //TODO: check if permission has changed
    }
}