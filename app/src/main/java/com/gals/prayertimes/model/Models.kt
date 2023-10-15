package com.gals.prayertimes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DateConfig(
    var dayName : String = "",
    var moonDate : String = "",
    var sunDate : String = ""
)

data class NextPrayerConfig(
    var isRamadan: Boolean = false,
    var isPrayer: Boolean = false,
    var isNight: Boolean = false,
    var nextPrayerName: String = "",
    var nextPrayerTime: String = "",
    var nextPrayerBanner: String = ""
)

data class MenuItem(
    @DrawableRes val icon: Int = 0,
    @StringRes val title: Int = 0,
    val navigateTo: () -> Unit = {}
)

enum class NotificationType(val value: String) {
    SILENT("silent"),
    TONE("tone"),
    HALF("half"),
    FULL("full")
}