package com.gals.prayertimes.utils

import android.text.format.DateUtils
import com.gals.prayertimes.R
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.config.NextPrayerConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.StringTokenizer
import javax.inject.Inject
import kotlin.math.ceil

class PrayerCalculation @Inject constructor(
    private val resourceProvider: ResourceProvider
) { //TODO: think over exception handling
    fun isNowEqualsTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().get(Calendar.MINUTE) == value.get(Calendar.MINUTE) - 1
                && getTimeNow().toCalendar()
            .get(Calendar.HOUR_OF_DAY) == value.get(Calendar.HOUR_OF_DAY)

    fun isNight(prayer: TimePrayer): Boolean =
        isNowAfterTime(prayer.maghreb) || isNowBeforeTime(prayer.sunrise)

    fun isRamadan(moonDate: String?): Boolean {
        try {
            val date = StringTokenizer(moonDate, ".")
            date.nextToken()
            if (date.nextToken().toInt() == MONTH_RAMADAN_NUMBER) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**Checks if the date changed at midnight*/
    fun isDayChanged(date: String?): Boolean {
        try {
            val currentDate = date?.let {
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.US
                ).parse(it)
            }
            if (currentDate != null) {
                return DateUtils.isToday(currentDate.time + DateUtils.DAY_IN_MILLIS)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return true // to trigger an Update when the date is not determined
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

    fun calculateNextPrayerInfo(currentPrayer: TimePrayer): NextPrayerConfig {
        val nextPrayerConfig = NextPrayerConfig(
            isNight = isNight(currentPrayer),
            isPrayer = isNextAPrayer(currentPrayer)
        )

        when {
            isNowBeforeTime(currentPrayer.fajer) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.fajer,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_fajer)
            }

            isNowBeforeTime(currentPrayer.sunrise) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.sunrise,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_day_sunrise)
                nextPrayerConfig.isPrayer = true
            }

            isNowBeforeTime(currentPrayer.duhr) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.duhr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_duhr)
            }

            isNowBeforeTime(currentPrayer.asr) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.asr,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_asr)
            }

            isNowBeforeTime(currentPrayer.maghreb) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.maghreb,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_maghrib)
            }

            isNowBeforeTime(currentPrayer.isha) -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.isha,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_prayer_isha)
            }

            isNowBeforeTime(currentPrayer.midNight) || getTimeNow().toCalendar() == currentPrayer.midNight -> {
                nextPrayerConfig.nextPrayerTime =
                    calculateDifferenceBetweenTimes(
                        currentPrayer.midNight,
                        getTimeNow().toCalendar()
                    )
                nextPrayerConfig.isNight = true
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_midnight_time_title)
            }

            else -> {
                nextPrayerConfig.nextPrayerTime = "00:00"
                nextPrayerConfig.isNight = true
                nextPrayerConfig.nextPrayerName =
                    resourceProvider.getString(R.string.text_midnight_time_title)
                nextPrayerConfig.nextPrayerBanner =
                    resourceProvider.getString(R.string.text_remaining_time)
                return nextPrayerConfig
            }
        }
        if (isNextAPrayer(currentPrayer)) {
            nextPrayerConfig.nextPrayerBanner =
                resourceProvider.getString(R.string.text_remaining_prayer_time)
        } else {
            nextPrayerConfig.nextPrayerBanner =
                resourceProvider.getString(R.string.text_remaining_time)
        }
        return nextPrayerConfig
    }

    private fun isNextAPrayer(prayer: TimePrayer): Boolean =
        !(isNextMidnight(prayer) || isNextSunrise(prayer))

    private fun isNextSunrise(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.sunrise) && isNowAfterTime(prayer.fajer)

    private fun isNextMidnight(prayer: TimePrayer): Boolean =
        isNowBeforeTime(prayer.midNight) && isNowAfterTime(prayer.isha)

    private fun isNowBeforeTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().before(value)

    private fun isNowAfterTime(value: Calendar): Boolean =
        getTimeNow().toCalendar().after(value)

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
