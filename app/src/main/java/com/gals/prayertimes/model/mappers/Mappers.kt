package com.gals.prayertimes.utils

import androidx.compose.ui.graphics.Color
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.UiPrayer
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun PrayerEntity.toPrayer(resourceProvider: ResourceProvider, formatter: Formatter): UiPrayer =
    UiPrayer(
        uiDate = UiDate(
            dayName = resourceProvider.getString(mapDayName(Calendar.getInstance()[Calendar.DAY_OF_WEEK])),
            moonDate = formatter.formatDateText(this.mDate, false),
            sunDate = formatter.formatDateText(this.sDate, true)
        ),
        prayers = mapOf(
            PrayerName.FAJER to this.fajer,
            PrayerName.SUNRISE to this.sunrise,
            PrayerName.DUHR to this.duhr,
            PrayerName.ASR to this.asr,
            PrayerName.MAGHRIB to this.maghrib,
            PrayerName.ISHA to this.isha
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
            else -> {/* no-op */
            }
        }
    }
    return entity
}

fun PrayerEntity.toTimePrayer(): TimePrayer = TimePrayer(
    fajer = this.fajer.toCalendar(),
    sunrise = this.sunrise.toCalendar(),
    duhr = this.duhr.toCalendar(),
    asr = this.asr.toCalendar(),
    maghrib = this.maghrib.toCalendar(),
    isha = this.isha.toCalendar()
)

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

fun todayDate(): String = SimpleDateFormat(
    "dd.MM.yyyy",
    Locale.US
).format(Date())

fun timeNow(): String = SimpleDateFormat(
    "HH:mm",
    Locale.US
).format(Date())

fun mapSunMonth(month: Int): Int =
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

fun mapMoonMonth(month: Int): Int =
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

fun mapDayName(day: Int): Int =
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

fun mapImage(isNight: Boolean, isRamadan: Boolean): Int =
    if (isRamadan) {
        if (isNight) R.drawable.ramadan_night else R.drawable.ramadan_day
    } else {
        if (isNight) R.drawable.background_night else R.drawable.background_day
    }

fun NextPrayerConfig.toUiNextPrayer(): UiNextPrayer =
    UiNextPrayer(
        backgroundImage = mapImage(isNight, isRamadan),
        settingsIconTint = if (isNight) Color.White else Color.Black,
        nextPrayerName = nextPrayerName,
        nextPrayerTime = nextPrayerTime,
        nextPrayerBanner = nextPrayerBanner
    )