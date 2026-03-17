package com.gals.prayertimes.utils

import android.app.Activity
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
private fun isTabletWidth(): Boolean =
    calculateWindowSizeClass(LocalActivity.current as Activity).widthSizeClass >= WindowWidthSizeClass.Medium

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun isTabletHeight(): Boolean =
    calculateWindowSizeClass(LocalActivity.current as Activity).heightSizeClass >= WindowHeightSizeClass.Medium
