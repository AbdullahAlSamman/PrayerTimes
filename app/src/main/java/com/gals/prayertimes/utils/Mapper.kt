package com.gals.prayertimes.utils

import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.repository.network.model.NetworkPrayer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun EntityPrayer.toDomain(): DomainPrayer = DomainPrayer(
    this.objectId,
    this.sDate,
    this.mDate,
    this.fajer,
    this.sunrise,
    this.duhr,
    this.asr,
    this.maghrib,
    this.isha
)

fun NetworkPrayer.toEntity(): EntityPrayer = EntityPrayer(
    objectId = this.objectId,
    sDate = this.sDate,
    mDate = this.mDate,
    fajer = this.fajer,
    sunrise = this.sunrise,
    duhr = this.duhr,
    asr = this.asr,
    maghrib = this.maghrib,
    isha = this.isha
)

fun String.toCalendar(): Calendar {
    val calendar: Calendar = Calendar.getInstance()
    val time = this.split(":".toRegex()).toTypedArray()
    calendar.set(
        Calendar.HOUR_OF_DAY,
        time[0].trim { it <= ' ' }.toInt()
    )
    calendar.set(
        Calendar.MINUTE,
        time[1].trim { it <= ' ' }.toInt()
    )
    return calendar
}

fun Calendar.setHourMinutes(hours: Int, minutes: Int): Calendar {
    this.set(Calendar.HOUR_OF_DAY, hours)
    this.set(Calendar.MINUTE, minutes)
    return this
}

fun getTodayDate(): String = SimpleDateFormat(
    "dd.MM.yyyy",
    Locale.US
).format(Date())

fun getTimeNow(): String = SimpleDateFormat(
    "HH:mm",
    Locale.US
).format(Date())

fun getSunMonth(month: Int): Int =
    when (month) {
        1 -> R.string.text_sun_month_jan
        2 -> R.string.text_sun_month_feb
        3 -> R.string.text_sun_month_mar
        4 -> R.string.text_sun_month_apr
        5 -> R.string.text_sun_month_may
        6 -> R.string.text_sun_month_jun
        7 -> R.string.text_sun_month_jul
        8 -> R.string.text_sun_month_aug
        9 -> R.string.text_sun_month_sep
        10 -> R.string.text_sun_month_oct
        11 -> R.string.text_sun_month_nov
        12 -> R.string.text_sun_month_dec
        else -> 0
    }

fun getMoonMonth(month: Int): Int =
    when (month) {
        1 -> R.string.text_moon_month_muhram
        2 -> R.string.text_moon_month_safer
        3 -> R.string.text_moon_month_rabi_aoul
        4 -> R.string.text_moon_month_rabi_aker
        5 -> R.string.text_moon_month_gamada_aoul
        6 -> R.string.text_moon_month_gamada_aker
        7 -> R.string.text_moon_month_rajb
        8 -> R.string.text_moon_month_shaban
        9 -> R.string.text_moon_month_ramadan
        10 -> R.string.text_moon_month_shual
        11 -> R.string.text_moon_month_zo_kada
        12 -> R.string.text_moon_month_zo_haga
        else -> 0
    }

fun getDayName(day: Int): Int =
    when (day) {
        Calendar.SATURDAY -> R.string.text_day_sat
        Calendar.SUNDAY -> R.string.text_day_sun
        Calendar.MONDAY -> R.string.text_day_mon
        Calendar.TUESDAY -> R.string.text_day_tue
        Calendar.WEDNESDAY -> R.string.text_day_web
        Calendar.THURSDAY -> R.string.text_day_thr
        Calendar.FRIDAY -> R.string.text_day_fri
        else -> 0
    }