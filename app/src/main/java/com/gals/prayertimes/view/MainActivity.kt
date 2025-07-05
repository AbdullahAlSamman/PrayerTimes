package com.gals.prayertimes.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gals.prayertimes.navigation.PrayerTimesNavHost
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrayerTimesTheme {
                val navController = rememberNavController()
                PrayerTimesNavHost(navController = navController, modifier = Modifier)
            }
        }
    }
}