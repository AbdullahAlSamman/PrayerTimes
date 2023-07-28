package com.gals.prayertimes.model.config

data class NextPrayerConfig(
    var isPrayer: Boolean = false,
    var isNight: Boolean = false,
    var nextPrayerName: String = "",
    var nextPrayerTime: String = "",
    var nextPrayerBanner: String = ""
)
