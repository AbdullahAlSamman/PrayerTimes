package com.gals.prayertimes.common

import com.gals.prayertimes.common.mappers.setHoursMinutes
import java.util.Calendar

enum class UiPrayerName {
    FAJER,
    SUNRISE,
    DUHR,
    ASR,
    MAGHRIB,
    ISHA;

    companion object Companion {
        fun fromString(value: String?): UiPrayerName =
            UiPrayerName.entries.find { it.name == value } ?: ISHA
    }
}

data class NextPrayerConfig(
    var isRamadan: Boolean = false,
    var isPrayer: Boolean = false,
    var isNight: Boolean = false,
    var nextPrayerName: String = "",
    var nextPrayerTime: String = "",
    var nextPrayerBanner: String = ""
)

enum class NotificationType {
    SILENT,
    TONE,
    HALF,
    FULL;

    companion object {
        fun fromString(value: String?): NotificationType =
            NotificationType.entries.find { it.name == value } ?: SILENT
    }
}

//TODO: replace with map prayer name as key and LocalDateTime as value.
data class TimePrayer(
    var fajer: Calendar = Calendar.getInstance(),
    var sunrise: Calendar = Calendar.getInstance(),
    var duhr: Calendar = Calendar.getInstance(),
    var asr: Calendar = Calendar.getInstance(),
    var maghrib: Calendar = Calendar.getInstance(),
    var isha: Calendar = Calendar.getInstance(),
    val midNight: Calendar = Calendar.getInstance().setHoursMinutes(23, 59)
)
