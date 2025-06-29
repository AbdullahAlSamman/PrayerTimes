package com.gals.prayertimes.model.mappers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.model.UiPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerNameResponse
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.ui.theme.colorBackgroundAsr
import com.gals.prayertimes.ui.theme.colorBackgroundDuhr
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.ui.theme.colorBackgroundMaghrib
import com.gals.prayertimes.ui.theme.colorBackgroundSunrise
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.ResourceProvider
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
            PrayerNameResponse.FAJER to this.fajer,
            PrayerNameResponse.SUNRISE to this.sunrise,
            PrayerNameResponse.DUHR to this.duhr,
            PrayerNameResponse.ASR to this.asr,
            PrayerNameResponse.MAGHRIB to this.maghrib,
            PrayerNameResponse.ISHA to this.isha
        )
    )

fun PrayersResponse.toEntity(): PrayerEntity {
    val entity = PrayerEntity(
        sDate = this.sDate,
        mDate = this.mDate,
    )
    this.prayers.forEach { prayer ->
        when (prayer.name) {
            PrayerNameResponse.FAJER -> entity.fajer = prayer.time
            PrayerNameResponse.SUNRISE -> entity.sunrise = prayer.time
            PrayerNameResponse.DUHR -> entity.duhr = prayer.time
            PrayerNameResponse.ASR -> entity.asr = prayer.time
            PrayerNameResponse.MAGHRIB -> entity.maghrib = prayer.time
            PrayerNameResponse.ISHA -> entity.isha = prayer.time
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

fun TimePrayer.getTimePrayerByName(
    prayerName: PrayerName
): Calendar = when (prayerName) {
    PrayerName.FAJER -> fajer
    PrayerName.SUNRISE -> sunrise
    PrayerName.DUHR -> duhr
    PrayerName.ASR -> asr
    PrayerName.MAGRIB -> maghrib
    PrayerName.ISHA -> isha
}

@Composable
fun mapPrayerColor(prayer: com.gals.prayertimes.repository.remote.model.PrayerNameResponse): Color =
    when (prayer) {
        PrayerNameResponse.FAJER -> colorBackgroundFajer
        PrayerNameResponse.SUNRISE -> colorBackgroundSunrise
        PrayerNameResponse.DUHR -> colorBackgroundDuhr
        PrayerNameResponse.ASR -> colorBackgroundAsr
        PrayerNameResponse.MAGHRIB -> colorBackgroundMaghrib
        PrayerNameResponse.ISHA -> colorBackgroundIsha
        PrayerNameResponse.UNKNOWN -> colorBackgroundFajer
    }

@Composable
fun mapPrayerName(prayerName: com.gals.prayertimes.repository.remote.model.PrayerNameResponse): String =
    when (prayerName) {
        PrayerNameResponse.FAJER -> stringResource(id = R.string.text_prayer_fajer)
        PrayerNameResponse.SUNRISE -> stringResource(id = R.string.text_prayer_sunrise)
        PrayerNameResponse.DUHR -> stringResource(id = R.string.text_prayer_duhr)
        PrayerNameResponse.ASR -> stringResource(id = R.string.text_prayer_asr)
        PrayerNameResponse.MAGHRIB -> stringResource(id = R.string.text_prayer_maghrib)
        PrayerNameResponse.ISHA -> stringResource(id = R.string.text_prayer_isha)
        PrayerNameResponse.UNKNOWN -> stringResource(id = R.string.text_prayer_isha)
    }

@Composable
fun mapUiPrayerName(prayerName: PrayerName): String = when (prayerName) {
    PrayerName.FAJER -> stringResource(id = R.string.text_prayer_fajer)
    PrayerName.SUNRISE -> stringResource(id = R.string.text_prayer_sunrise)
    PrayerName.DUHR -> stringResource(id = R.string.text_prayer_duhr)
    PrayerName.ASR -> stringResource(id = R.string.text_prayer_asr)
    PrayerName.MAGRIB -> stringResource(id = R.string.text_prayer_maghrib)
    PrayerName.ISHA -> stringResource(id = R.string.text_prayer_isha)
}

@Composable
fun mapStatusBarColors(): Pair<Color, Boolean> {
    val color = if (isSystemInDarkTheme()) {
        colorResource(id = R.color.background_color_time_isha)
    } else {
        colorResource(id = R.color.background_color_time_fajer)
    }
    return (color to isSystemInDarkTheme())
}

@Composable
fun mapNotificationTypeText(notificationType: NotificationType): String =
    when (notificationType) {
        NotificationType.SILENT -> stringResource(id = R.string.text_alarm_silent)
        NotificationType.TONE -> stringResource(id = R.string.text_alarm_tone)
        NotificationType.HALF -> stringResource(id = R.string.text_alarm_half_athan)
        NotificationType.FULL -> stringResource(id = R.string.text_alarm_full_athan)
    }
