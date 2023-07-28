package com.gals.prayertimes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
    @DrawableRes val icon: Int = 0,
    @StringRes val title: Int = 0,
    val navigateTo: () -> Unit = {}
)