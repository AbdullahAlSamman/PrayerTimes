package com.gals.prayertimes.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.repository.remote.model.SinglePrayer
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.ui.theme.colorBackgroundAsr
import com.gals.prayertimes.ui.theme.colorBackgroundDuhr
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.ui.theme.colorBackgroundMaghrib
import com.gals.prayertimes.ui.theme.colorBackgroundSunrise

@Composable
fun getImage(isNight: Boolean, isRamadan: Boolean): Painter {
    val resource = if (isRamadan) {
        if (isNight) R.drawable.ramadan_night else R.drawable.ramadan_day
    } else {
        if (isNight) R.drawable.background_night else R.drawable.background_day
    }
    return painterResource(id = resource)
}

@Composable
fun prayerColorMapper(prayer: SinglePrayer): Color =
    when (prayer.name) {
        PrayerName.FAJER -> colorBackgroundFajer
        PrayerName.SUNRISE -> colorBackgroundSunrise
        PrayerName.DUHR -> colorBackgroundDuhr
        PrayerName.ASR -> colorBackgroundAsr
        PrayerName.MAGHRIB -> colorBackgroundMaghrib
        PrayerName.ISHA -> colorBackgroundIsha
        PrayerName.UNKNOWN -> colorBackgroundFajer
    }

@Composable
fun prayerNameMapper(prayerName: PrayerName): String =
    when (prayerName) {
        PrayerName.FAJER -> stringResource(id = R.string.text_prayer_fajer)
        PrayerName.SUNRISE -> stringResource(id = R.string.text_prayer_sunrise)
        PrayerName.DUHR -> stringResource(id = R.string.text_prayer_duhr)
        PrayerName.ASR -> stringResource(id = R.string.text_prayer_asr)
        PrayerName.MAGHRIB -> stringResource(id = R.string.text_prayer_maghrib)
        PrayerName.ISHA -> stringResource(id = R.string.text_prayer_isha)
        PrayerName.UNKNOWN -> stringResource(id = R.string.text_prayer_isha)
    }

@Composable
fun getStatusBarColors(): Pair<Color, Boolean> {
    val color = if (isSystemInDarkTheme()) {
        colorResource(id = R.color.background_color_time_isha)
    } else {
        colorResource(id = R.color.background_color_time_fajer)
    }
    return (color to isSystemInDarkTheme())
}
@Composable
fun getNotificationTypeText(notificationType: NotificationType): String =
    when(notificationType){
        NotificationType.SILENT -> stringResource(id = R.string.text_alarm_silent)
        NotificationType.TONE -> stringResource(id = R.string.text_alarm_tone)
        NotificationType.HALF -> stringResource(id = R.string.text_alarm_half_athan)
        NotificationType.FULL -> stringResource(id = R.string.text_alarm_full_athan)
    }