package com.gals.prayertimes.model

import com.gals.prayertimes.repository.remote.model.SinglePrayer
import com.gals.prayertimes.utils.setHoursMinutes
import java.util.Calendar

data class Prayer(
    var sDate: String = "",
    var mDate: String = "",
    var prayers: List<SinglePrayer> = emptyList()
)

data class TimePrayer(
    var fajer: Calendar = Calendar.getInstance(),
    var sunrise: Calendar = Calendar.getInstance(),
    var duhr: Calendar = Calendar.getInstance(),
    var asr: Calendar = Calendar.getInstance(),
    var maghreb: Calendar = Calendar.getInstance(),
    var isha: Calendar = Calendar.getInstance(),
    val midNight: Calendar = Calendar.getInstance().setHoursMinutes(23, 59)
)