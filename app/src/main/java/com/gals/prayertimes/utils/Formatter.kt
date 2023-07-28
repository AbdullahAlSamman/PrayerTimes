package com.gals.prayertimes.utils

import com.gals.prayertimes.R
import com.gals.prayertimes.viewmodel.MainViewModel
import java.util.StringTokenizer
import javax.inject.Inject


class Formatter @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun formatDateText(
        date: String,
        isSunCalendar: Boolean
    ): String {
        val dateList = StringTokenizer(date, MainViewModel.STRING_DATE_SEPARATOR).toList()
            .map { it.toString() }
        val month: Int
        val footer: String
        if (isSunCalendar) {
            month = getSunMonth(dateList[1].toInt())
            footer = resourceProvider.getString(R.string.text_sun_month_footer)
        } else {
            month = getMoonMonth(dateList[1].toInt())
            footer = resourceProvider.getString(R.string.text_moon_month_footer)
        }

        return "${dateList[0]} ${resourceProvider.getString(month)} ${dateList[2]} $footer"
    }

}