package com.gals.prayertimes.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.gals.prayertimes.navigation.PrayerTimesNavHost
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.utils.upAPILevel29
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (upAPILevel29) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                PrayerTimesTheme {
                    PrayerTimesNavHost()
                }
            }
        }
    }
}