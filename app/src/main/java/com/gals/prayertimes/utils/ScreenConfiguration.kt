package com.gals.prayertimes.utils

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

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
    val activity = LocalActivity.current ?: return LocalConfiguration.current.screenWidthDp >= 600
    return calculateWindowSizeClass(activity).widthSizeClass >= WindowWidthSizeClass.Medium
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun isTabletHeight(): Boolean {
    val activity = LocalActivity.current ?: return LocalConfiguration.current.screenHeightDp >= 480
    return calculateWindowSizeClass(activity).heightSizeClass >= WindowHeightSizeClass.Medium
}
