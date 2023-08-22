package com.gals.prayertimes.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class UiNextPrayer(
    @DrawableRes val backgroundImage: Int = 0,
    val settingsIconTint: Color = Color.Black,
    val nextPrayerName: String = "",
    val nextPrayerTime: String = "",
    val nextPrayerBanner: String = ""
)

data class UiDate(
    val dayName : String = "",
    val moonDate : String = "",
    val sunDate : String = ""
)