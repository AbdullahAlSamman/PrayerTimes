package com.gals.prayertimes.utils

import android.content.Context
import com.gals.prayertimes.R
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.config.NextPrayerInfoConfig
import java.util.Calendar
import java.util.StringTokenizer
import kotlin.math.ceil

class PrayerCalculation(
    private val applicationContext: Context
) {
    fun isNextAPrayer(prayer: TimePrayer): Boolean =
        !(isNextMidnight(prayer) || isNextSunrise(prayer))

    fun isNextSunrise(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.sunrise) && isNowAfterTime(prayer.fajer)

    fun isNextMidnight(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.midNight) && isNowAfterTime(prayer.isha)

    fun isNight(prayer: TimePrayer): Boolean =
        isNowAfterTime(prayer.maghreb) || isNowBeforeTime(prayer.sunrise)

    fun isNowBeforeTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().before(value)

    fun isNowAfterTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().after(value)

    fun isNowEqualsTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().get(Calendar.MINUTE) == value.get(Calendar.MINUTE) - 1
                && getTimeNow().toCalendar()
            .get(Calendar.HOUR_OF_DAY) == value.get(Calendar.HOUR_OF_DAY)

    fun isRamadan(moonDate: String?): Boolean {
        try {
            val mdate = StringTokenizer(moonDate, ".")
            mdate.nextToken()
            if (mdate.nextToken().toInt() == MONTH_RAMADAN_NUMBER) {
                return true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun calculateNextPrayer(currentPrayer: TimePrayer): Calendar =
        when {
            isNowBeforeTime(currentPrayer.fajer) -> currentPrayer.fajer
            isNowBeforeTime(currentPrayer.sunrise) -> currentPrayer.sunrise
            isNowBeforeTime(currentPrayer.duhr) -> currentPrayer.duhr
            isNowBeforeTime(currentPrayer.asr) -> currentPrayer.asr
            isNowBeforeTime(currentPrayer.maghreb) -> currentPrayer.maghreb
            isNowBeforeTime(currentPrayer.isha) -> currentPrayer.isha
            isNowBeforeTime(currentPrayer.midNight) || getTimeNow().toCalendar() == currentPrayer.midNight ->
                currentPrayer.midNight
            else -> Calendar.getInstance()
        }

    fun calculateNextPrayerInfo(currentPrayer: TimePrayer): NextPrayerInfoConfig {
        val nextPrayerInfoConfig = NextPrayerInfoConfig()
        when {
            isNowBeforeTime(currentPrayer.fajer) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.fajer,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_fajer)
            }
            isNowBeforeTime(currentPrayer.sunrise) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.sunrise,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_day_sunrise)
                nextPrayerInfoConfig.isSunrise = true
            }
            isNowBeforeTime(currentPrayer.duhr) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.duhr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_duhr)
            }
            isNowBeforeTime(currentPrayer.asr) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.asr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_asr)
            }
            isNowBeforeTime(currentPrayer.maghreb) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.maghreb,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_maghrib)
            }
            isNowBeforeTime(currentPrayer.isha) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.isha,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_prayer_isha)
            }
            isNowBeforeTime(currentPrayer.midNight) || getTimeNow().toCalendar() == currentPrayer.midNight -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.midNight,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.isMidnight = true
                nextPrayerInfoConfig.nextPrayerNameText =
                    applicationContext.getString(R.string.text_midnight_time_title)
            }
        }
        if (isNextAPrayer(currentPrayer)) {
            nextPrayerInfoConfig.nextPrayerBannerText =
                applicationContext.getString(R.string.text_remaining_prayer_time)
        } else {
            nextPrayerInfoConfig.nextPrayerBannerText =
                applicationContext.getString(R.string.text_remaining_time)
        }
        return nextPrayerInfoConfig
    }

    private fun calculateDifferenceBetweenTimes(
        big: Calendar,
        small: Calendar
    ): String {
        val result = big.timeInMillis - small.timeInMillis
        val intHours = ceil((result / (1000 * 60 * 60)).toDouble()).toInt()
        val intMinutes = ceil((result / (1000 * 60) % 60).toDouble()).toInt()
        var minutes = intMinutes.toString() + ""
        var hours = intHours.toString() + ""
        if (intMinutes < 10) {
            minutes = "0$intMinutes"
        }
        if (intHours < 10) {
            hours = "0$intHours"
        }
        return "$hours:$minutes"
    }

    companion object {
        private const val MONTH_RAMADAN_NUMBER = 9
    }
}
