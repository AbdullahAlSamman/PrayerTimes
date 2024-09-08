package com.gals.prayertimes.model

import com.gals.prayertimes.model.mappers.setHoursMinutes
import java.util.Calendar

data class NextPrayerConfig(
    var isRamadan: Boolean = false,
    var isPrayer: Boolean = false,
    var isNight: Boolean = false,
    var nextPrayerName: String = "",
    var nextPrayerTime: String = "",
    var nextPrayerBanner: String = ""
)

enum class NotificationType(val value: String) {
    SILENT("silent"),
    TONE("tone"),
    HALF("half"),
    FULL("full")
}

data class TimePrayer(
    var fajer: Calendar = Calendar.getInstance(),
    var sunrise: Calendar = Calendar.getInstance(),
    var duhr: Calendar = Calendar.getInstance(),
    var asr: Calendar = Calendar.getInstance(),
    var maghrib: Calendar = Calendar.getInstance(),
    var isha: Calendar = Calendar.getInstance(),
    val midNight: Calendar = Calendar.getInstance().setHoursMinutes(23, 59)
)