package com.gals.prayertimes.utils

import com.gals.prayertimes.R
import com.gals.prayertimes.common.mappers.mapMoonMonth
import com.gals.prayertimes.common.mappers.mapSunMonth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.StringTokenizer
import javax.inject.Inject

class DateFormatter @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    fun formatSunDateText(date: String): String =
        formatDateText(
            date = date,
            monthMapper = ::mapSunMonth,
            footerResId = R.string.text_sun_month_footer
        )

    fun formatMoonDateText(date: String): String =
        formatDateText(
            date = date,
            monthMapper = ::mapMoonMonth,
            footerResId = R.string.text_moon_month_footer
        )

    fun formatMillisToDateString(millis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY)
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
            .format(formatter)
    }

    private fun formatDateText(date: String, monthMapper: (Int) -> Int, footerResId: Int): String {
        val dateList = StringTokenizer(date, STRING_DATE_SEPARATOR)
            .toList()
            .map { it.toString() }
        val month = monthMapper(dateList[1].toInt())
        val footer = resourceProvider.getString(footerResId)
        return "${dateList[0]} ${resourceProvider.getString(month)} ${dateList[2]} $footer"
    }
}

private const val STRING_DATE_SEPARATOR = "."