package com.gals.prayertimes.utils

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

val checkAPILevelForAlarms: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S