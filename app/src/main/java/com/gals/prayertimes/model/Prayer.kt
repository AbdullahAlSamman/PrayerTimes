package com.gals.prayertimes.model

import androidx.compose.runtime.Immutable
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.utils.setHoursMinutes
import java.util.Calendar


@Immutable
data class Prayer(
    var uiDate: UiDate = UiDate(),
    var prayers: Map<PrayerName, String> = emptyMap()
)

data class TimePrayer(
    var fajer: Calendar = Calendar.getInstance(),
    var sunrise: Calendar = Calendar.getInstance(),
    var duhr: Calendar = Calendar.getInstance(),
    var asr: Calendar = Calendar.getInstance(),
    var maghrib: Calendar = Calendar.getInstance(),
    var isha: Calendar = Calendar.getInstance(),
    val midNight: Calendar = Calendar.getInstance().setHoursMinutes(23, 59)
)