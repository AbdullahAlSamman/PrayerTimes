package com.gals.prayertimes.model.config

data class NextPrayerInfoConfig(
    var isSunrise: Boolean = false,
    var nextPrayerNameText: String = "",
    var nextPrayerTime: String = "",
    var nextPrayerBannerText: String = ""
)
