package com.gals.prayertimes.utils

import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp

val checkAPILevelForAlarms: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun Modifier.applyDefaultPadding(value: Dp) = Modifier.padding(start = value, end = value)