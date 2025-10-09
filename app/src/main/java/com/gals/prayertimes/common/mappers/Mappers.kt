package com.gals.prayertimes.common.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.gals.prayertimes.R
import com.gals.prayertimes.common.NextPrayerConfig
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.TimePrayer
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.main.model.UiPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerNameResponse
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.services.alarmmanager.AlarmItem
import com.gals.prayertimes.services.alarmmanager.PrayerAlarmItem
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
            UiPrayerName.FAJER to this.fajer,
            UiPrayerName.SUNRISE to this.sunrise,
            UiPrayerName.DUHR to this.duhr,
            UiPrayerName.ASR to this.asr,
            UiPrayerName.MAGHRIB to this.maghrib,
            UiPrayerName.ISHA to this.isha
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
    prayerName: UiPrayerName
): Calendar = when (prayerName) {
    UiPrayerName.FAJER -> fajer
    UiPrayerName.SUNRISE -> sunrise
    UiPrayerName.DUHR -> duhr
    UiPrayerName.ASR -> asr
    UiPrayerName.MAGHRIB -> maghrib
    UiPrayerName.ISHA -> isha
}

fun UiPrayerName.getStringId(): Int =
    when (this) {
        UiPrayerName.FAJER -> R.string.text_prayer_fajer
        UiPrayerName.SUNRISE -> R.string.text_prayer_sunrise
        UiPrayerName.DUHR -> R.string.text_prayer_duhr
        UiPrayerName.ASR -> R.string.text_prayer_asr
        UiPrayerName.MAGHRIB -> R.string.text_prayer_maghrib
        UiPrayerName.ISHA -> R.string.text_prayer_isha
    }

fun PrayerAlarmItem.toAlarmItem(): AlarmItem =
    AlarmItem(time = time, prayer = prayer)

@Composable
fun mapPrayerColor(prayer: UiPrayerName): Color =
    when (prayer) {
        UiPrayerName.FAJER -> colorBackgroundFajer
        UiPrayerName.SUNRISE -> colorBackgroundSunrise
        UiPrayerName.DUHR -> colorBackgroundDuhr
        UiPrayerName.ASR -> colorBackgroundAsr
        UiPrayerName.MAGHRIB -> colorBackgroundMaghrib
        UiPrayerName.ISHA -> colorBackgroundIsha
    }

@Composable
fun mapUiPrayerName(prayerName: UiPrayerName): String =
    when (prayerName) {
        UiPrayerName.FAJER -> stringResource(id = R.string.text_prayer_fajer)
        UiPrayerName.SUNRISE -> stringResource(id = R.string.text_prayer_sunrise)
        UiPrayerName.DUHR -> stringResource(id = R.string.text_prayer_duhr)
        UiPrayerName.ASR -> stringResource(id = R.string.text_prayer_asr)
        UiPrayerName.MAGHRIB -> stringResource(id = R.string.text_prayer_maghrib)
        UiPrayerName.ISHA -> stringResource(id = R.string.text_prayer_isha)
    }

@Composable
fun mapNotificationTypeText(notificationType: NotificationType): String =
    when (notificationType) {
        NotificationType.SILENT -> stringResource(id = R.string.text_alarm_silent)
        NotificationType.TONE -> stringResource(id = R.string.text_alarm_tone)
        NotificationType.HALF -> stringResource(id = R.string.text_alarm_half_athan)
        NotificationType.FULL -> stringResource(id = R.string.text_alarm_full_athan)
    }