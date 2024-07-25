package com.gals.prayertimes.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun isTabletInLandscape(): Boolean = isTablet() && isLandscape()

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun isTablet(): Boolean = when (windowWidthSizeClass()) {
    WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> true
    else -> false
}

@ExperimentalMaterial3WindowSizeClassApi
@Composable
private fun windowWidthSizeClass(): WindowWidthSizeClass? {
    return LocalContext.current.getActivity()?.let { activity ->
        val windowSizeClass = calculateWindowSizeClass(activity)
        windowSizeClass.widthSizeClass
    }
}

@Composable
fun isLandscape(): Boolean = when (LocalConfiguration.current.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> true
    else -> false
}

private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

