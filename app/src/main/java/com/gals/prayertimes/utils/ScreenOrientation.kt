package com.gals.prayertimes.utils

import android.content.res.Configuration
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun isLandscape(): Boolean = when (LocalConfiguration.current.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> true
    else -> false
}

@Composable
fun isTablet(): Boolean = isTabletWidth() && isTabletHeight()

@Composable
fun isTabletInPortrait(): Boolean = isTablet() && !isLandscape()

@Composable
fun isPhoneInLandscape(): Boolean = !isTablet() && isLandscape()

@Composable
private fun isTabletWidth(): Boolean = when (getWindowWidthClassSize()) {
    WindowWidthSizeClass.MEDIUM, WindowWidthSizeClass.EXPANDED -> true
    else -> false
}

@Composable
private fun isTabletHeight(): Boolean = when (getWindowHeightClassSize()) {
    WindowHeightSizeClass.MEDIUM, WindowHeightSizeClass.EXPANDED -> true
    else -> false
}

@Composable
private fun getWindowWidthClassSize(): WindowWidthSizeClass =
    currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

@Composable
private fun getWindowHeightClassSize(): WindowHeightSizeClass =
    currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass