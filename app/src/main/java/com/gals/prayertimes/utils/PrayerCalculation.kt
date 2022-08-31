package com.gals.prayertimes.utils

import android.content.Context
import com.gals.prayertimes.R
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.config.NextPrayerInfoConfig
import java.util.Calendar
import kotlin.math.ceil

class PrayerCalculation(
    private val application: Context
) {
    fun isNextAPrayer(prayer: TimePrayer): Boolean =
        !(isNextMidnight(prayer) || isNextSunrise(prayer))

    fun isNextSunrise(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.sunrise) && isNowAfterTime(prayer.fajer)

    fun isNextMidnight(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.midNight) && isNowAfterTime(prayer.isha)

    fun isNight(prayer: TimePrayer): Boolean =
        !isNowBeforeTime(prayer.maghreb) && isNowAfterTime(prayer.sunrise)

    fun isNowBeforeTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().before(value)

    private fun isNowAfterTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().after(value)

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
                    application.getString(R.string.text_prayer_fajer)
            }
            isNowBeforeTime(currentPrayer.sunrise) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.sunrise,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_prayer_day_sunrise)
            }
            isNowBeforeTime(currentPrayer.duhr) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.duhr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_prayer_duhr)
            }
            isNowBeforeTime(currentPrayer.asr) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.asr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_prayer_asr)
            }
            isNowBeforeTime(currentPrayer.maghreb) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.maghreb,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_prayer_maghrib)
            }
            isNowBeforeTime(currentPrayer.isha) -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.isha,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_prayer_isha)
            }
            isNowBeforeTime(currentPrayer.midNight) || getTimeNow().toCalendar() == currentPrayer.midNight -> {
                nextPrayerInfoConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.midNight,
                        getTimeNow().toCalendar()
                    )
                nextPrayerInfoConfig.nextPrayerNameText =
                    application.getString(R.string.text_midnight_time_title)
            }
        }
        if (isNextAPrayer(currentPrayer)) {
            nextPrayerInfoConfig.nextPrayerBannerText =
                application.getString(R.string.text_remaining_prayer_time)
        } else {
            nextPrayerInfoConfig.nextPrayerBannerText =
                application.getString(R.string.text_remaining_time)
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
}
