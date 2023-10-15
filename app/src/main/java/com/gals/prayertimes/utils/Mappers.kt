package com.gals.prayertimes.utils

import androidx.compose.ui.graphics.Color
import com.gals.prayertimes.R
import com.gals.prayertimes.model.DateConfig
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.repository.remote.model.SinglePrayer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun PrayerEntity.toPrayer(): Prayer = Prayer(
    sDate = this.sDate,
    mDate = this.mDate,
    prayers = listOf(
        SinglePrayer(name = PrayerName.FAJER, time = this.fajer),
        SinglePrayer(name = PrayerName.SUNRISE, time = this.sunrise),
        SinglePrayer(name = PrayerName.DUHR, time = this.duhr),
        SinglePrayer(name = PrayerName.ASR, time = this.asr),
        SinglePrayer(name = PrayerName.MAGHRIB, time = this.maghrib),
        SinglePrayer(name = PrayerName.ISHA, time = this.isha)
    )
)

fun PrayersResponse.toEntity(): PrayerEntity {
    val entity = PrayerEntity(
        sDate = this.sDate,
        mDate = this.mDate,
    )
    this.prayers.forEach { prayer ->
        when (prayer.name) {
            PrayerName.FAJER -> entity.fajer = prayer.time
            PrayerName.SUNRISE -> entity.sunrise = prayer.time
            PrayerName.DUHR -> entity.duhr = prayer.time
            PrayerName.ASR -> entity.asr = prayer.time
            PrayerName.MAGHRIB -> entity.maghrib = prayer.time
            PrayerName.ISHA -> entity.isha = prayer.time
            else -> {}
        }
    }
    return entity
}

fun Prayer.toTimePrayer(): TimePrayer {
    val timePrayer = TimePrayer()
    this.prayers.forEach { prayer ->
        when (prayer.name) {
            PrayerName.FAJER -> timePrayer.fajer = prayer.time.toCalendar()
            PrayerName.SUNRISE -> timePrayer.sunrise = prayer.time.toCalendar()
            PrayerName.DUHR -> timePrayer.duhr = prayer.time.toCalendar()
            PrayerName.ASR -> timePrayer.asr = prayer.time.toCalendar()
            PrayerName.MAGHRIB -> timePrayer.maghreb = prayer.time.toCalendar()
            PrayerName.ISHA -> timePrayer.isha = prayer.time.toCalendar()
            else -> {}
        }
    }
    return timePrayer
}

fun String.toCalendar(): Calendar {
    val time = this.split(":".toRegex()).toTypedArray()
    return Calendar.getInstance().setHoursMinutes(
        time[0].trim { it <= ' ' }.toInt(),
        time[1].trim { it <= ' ' }.toInt()
    )
}

fun Calendar.setHoursMinutes(hours: Int, minutes: Int): Calendar {
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

fun getImage(isNight: Boolean, isRamadan: Boolean): Int =
    if (isRamadan) {
        if (isNight) R.drawable.ramadan_night else R.drawable.ramadan_day
    } else {
        if (isNight) R.drawable.background_night else R.drawable.background_day
    }

fun NextPrayerConfig.toUiNextPrayer(): UiNextPrayer =
    UiNextPrayer(
        backgroundImage = getImage(isNight, isRamadan),
        settingsIconTint = if (isNight) Color.White else Color.Black,
        nextPrayerName = nextPrayerName,
        nextPrayerTime = nextPrayerTime,
        nextPrayerBanner = nextPrayerBanner
    )

fun DateConfig.toUiDate(): UiDate =
    UiDate(
        dayName = dayName,
        moonDate = moonDate,
        sunDate = sunDate
    )
