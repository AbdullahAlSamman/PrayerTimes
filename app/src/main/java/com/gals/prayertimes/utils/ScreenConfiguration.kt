package com.gals.prayertimes.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun isLandscape(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

@Composable
fun isTablet(): Boolean = isTabletWidth() && isTabletHeight()

@Composable
fun isTabletInPortrait(): Boolean = isTablet() && !isLandscape()

@Composable
fun isPhoneInLandscape(): Boolean = !isTablet() && isLandscape()

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun isTabletWidth(): Boolean {
    val activity = LocalContext.current.findActivity() ?: return false
    return calculateWindowSizeClass(activity).widthSizeClass >= WindowWidthSizeClass.Medium
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun isTabletHeight(): Boolean {
    val activity = LocalContext.current.findActivity() ?: return false
    return calculateWindowSizeClass(activity).heightSizeClass >= WindowHeightSizeClass.Medium
}

/**
 * Helper to find the Activity from any Context (including wrappers)
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}