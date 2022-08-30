package com.gals.prayertimes.model

import com.gals.prayertimes.utils.setHourMinutes
import java.util.Calendar

data class TimePrayer(
    val fajer: Calendar = Calendar.getInstance(),
    val sunrise: Calendar = Calendar.getInstance(),
    val duhr: Calendar = Calendar.getInstance(),
    val asr: Calendar = Calendar.getInstance(),
    val maghreb: Calendar = Calendar.getInstance(),
    val isha: Calendar = Calendar.getInstance(),
    val midNight: Calendar = Calendar.getInstance().setHourMinutes(23, 59)
)