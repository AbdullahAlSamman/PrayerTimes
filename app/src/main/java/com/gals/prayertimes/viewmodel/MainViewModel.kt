package com.gals.prayertimes.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.entities.Prayer.Companion.isValid
import com.gals.prayertimes.utils.*
import com.gals.prayertimes.view.Menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val application: Context
) : ViewModel() {
    private val tools: UtilsManager = UtilsManager(application)
    private val repository: Repository = Repository(
        database = AppDB.getInstance(application)
    )
    private var sunriseTime: Calendar = Calendar.getInstance()
    private var midNightTime: Calendar = Calendar.getInstance()
    private var domainPrayer: DomainPrayer = DomainPrayer.EMPTY
    private lateinit var updateUITimer: Timer
    private lateinit var fajerTime: Calendar
    private lateinit var duhrTime: Calendar
    private lateinit var asrTime: Calendar
    private lateinit var sunsetTime: Calendar
    private lateinit var ishaTime: Calendar
    private lateinit var currentTime: Calendar
    private lateinit var remainingPrayerTime: String
    var backgroundImage: ObservableInt = ObservableInt()
    var btnSettingsResource: ObservableInt = ObservableInt()
    var sunDateBanner: ObservableField<String> = ObservableField()
    var moonDateBanner: ObservableField<String> = ObservableField()
    var nextPrayerTime: ObservableField<String> = ObservableField()
    var nextPrayerBanner: ObservableField<String> = ObservableField()
    var currentPrayerName: ObservableField<String> = ObservableField()
    var viewDayName: ObservableField<String> = ObservableField()
    var viewFajerTime: ObservableField<String> = ObservableField()
    var viewSunriseTime: ObservableField<String> = ObservableField()
    var viewDuhrTime: ObservableField<String> = ObservableField()
    var viewAsrTime: ObservableField<String> = ObservableField()
    var viewMaghribTime: ObservableField<String> = ObservableField()
    var viewIshaTime: ObservableField<String> = ObservableField()

    fun navigateToSettings() {
        application.startActivity(
            Intent(
                application,
                Menu::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun getPrayer() {
        var prayer: EntityPrayer?
        var waiting = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                prayer = repository.getPrayerFromLocalDataSource(getTodayDate())
                if (prayer != null) {
                    waiting = prayer!!.isValid()
                }
            }
            withContext(Dispatchers.Main) {
                if (waiting) {
                    updateViewObservableValues(prayer!!.toDomain())
                    startDateUpdate()
                }
            }
        }
    }

    /**Set data on the UI Elements*/
    internal fun updateDateUIObservables() = try {
        Log.i(
            "isChangeTheDayTest",
            "" + isDayHasChanged(domainPrayer.sDate)
        )
        if (isDayHasChanged(domainPrayer.sDate)) {
            updatePrayers()
        }
        buildDateTexts()
        updateRemainingTime()
        updateBackground()
    } catch (e: Exception) {
        Log.i(
            "Data Update UI",
            "there is no data at all"
        )
        e.printStackTrace()
    }

    /**Timer to update the ui*/
    private fun startDateUpdate(time: Int = 25000) {
        /**TODO:Not right to create a time handler everytime memory usage*/
        val handler = Handler(Looper.myLooper()!!)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        updateDateUIObservables()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        updateUITimer = Timer()
        updateUITimer.schedule(
            timerTask,
            0,
            time.toLong()
        )
    }

    private fun updateViewObservableValues(prayer: DomainPrayer) {
        domainPrayer = prayer
        viewFajerTime.set(prayer.fajer)
        viewSunriseTime.set(prayer.sunrise)
        viewDuhrTime.set(prayer.duhr)
        viewAsrTime.set(prayer.asr)
        viewMaghribTime.set(prayer.maghrib)
        viewIshaTime.set(prayer.isha)
        buildDateTexts()
    }

    private fun updatePrayers() {
        /** update the data from server if the date is changed*/
        if (tools.isNetworkAvailable()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (repository.refreshPrayer(getTodayDate())) {
                        domainPrayer =
                            repository.getPrayerFromLocalDataSource(getTodayDate())!!.toDomain()
                        if (domainPrayer != DomainPrayer.EMPTY) {
                            buildDateTexts()
                            updateViewObservableValues(domainPrayer)
                        }
                    }
                }
            }
        }
    }

    /**update the time to next prayer*/
    private fun updateRemainingTime() {
        if (calculateTimeBetweenPrayers()) {
            if (isNextAPrayer()) {
                nextPrayerTime.set(remainingPrayerTime)
                nextPrayerBanner.set(application.getString(R.string.text_remaining_prayer_time))
            } else {
                nextPrayerTime.set(remainingPrayerTime)
                nextPrayerBanner.set(application.getString(R.string.text_remaining_time))
            }
        }
    }

    private fun isNight(): Boolean {
        try {
            currentTime = getTodayDate().toCalendar()
            if (domainPrayer.sunrise != null || domainPrayer.maghrib != null) {
                Log.i(
                    "Prayer/Current Time ",
                    currentTime.time.toString()
                )
                return !(currentTime.before(sunsetTime) && currentTime.after(sunriseTime))
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return false
    }

    private fun isNextSunrise(): Boolean = try {
        currentTime = getTodayDate().toCalendar()
        currentTime.before(sunriseTime) && currentTime.after(fajerTime)
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        false
    }

    private fun isNextAPrayer(): Boolean = !(isNextMidnight() || isNextSunrise())

    private fun isNextMidnight(): Boolean =
        try {
            currentTime = getTimeNow().toCalendar()
            currentTime.before(midNightTime) && currentTime.after(ishaTime)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            false
        }

    private fun calculateTimeBetweenPrayers(): Boolean = try {
        currentTime = getTimeNow().toCalendar()
        // to update calender instances if the times are updated
        setCalenderPrayersTime()
        if (currentTime.before(fajerTime)) {
            remainingPrayerTime = tools.difTimes(
                fajerTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_fajer))
            true
        } else if (currentTime.before(sunriseTime)) {
            remainingPrayerTime = tools.difTimes(
                sunriseTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_day_sunrise))
            true
        } else if (currentTime.before(duhrTime)) {
            remainingPrayerTime = tools.difTimes(
                duhrTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_duhr))
            true
        } else if (currentTime.before(asrTime)) {
            remainingPrayerTime = tools.difTimes(
                asrTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_asr))
            true
        } else if (currentTime.before(sunsetTime)) {
            remainingPrayerTime = tools.difTimes(
                sunsetTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_maghrib))
            true
        } else if (currentTime.before(ishaTime)) {
            remainingPrayerTime = tools.difTimes(
                ishaTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_prayer_isha))
            true
        } else if (currentTime.before(midNightTime) || currentTime == midNightTime) {
            remainingPrayerTime = tools.difTimes(
                midNightTime,
                currentTime
            )
            currentPrayerName.set(application.getString(R.string.text_midnight_time_title))
            true
        } else {
            Log.e(
                "Calc1",
                "The value is False"
            )
            false
        }
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        Log.e(
            "Calc2",
            "The value is False"
        )
        false
    }

    private fun buildDateTexts(): Boolean {
        return try {
            lateinit var sunDateText: String
            lateinit var moonDateText: String
            val calendar = Calendar.getInstance()
            when (calendar[Calendar.DAY_OF_WEEK]) {
                Calendar.SATURDAY -> viewDayName.set(application.getString(R.string.text_day_sat))
                Calendar.SUNDAY -> viewDayName.set(application.getString(R.string.text_day_sun))
                Calendar.MONDAY -> viewDayName.set(application.getString(R.string.text_day_mon))
                Calendar.TUESDAY -> viewDayName.set(application.getString(R.string.text_day_tue))
                Calendar.WEDNESDAY -> viewDayName.set(application.getString(R.string.text_day_web))
                Calendar.THURSDAY -> viewDayName.set(application.getString(R.string.text_day_thr))
                Calendar.FRIDAY -> viewDayName.set(application.getString(R.string.text_day_fri))
                else -> {}
            }
            val sdate = StringTokenizer(
                domainPrayer.sDate,
                "."
            )
            val mdate = StringTokenizer(
                domainPrayer.mDate,
                "."
            )
            sunDateText = sdate.nextToken() + " "
            moonDateText = mdate.nextToken() + " "
            when (sdate.nextToken().toInt()) {
                1 -> sunDateText += application.getString(R.string.text_sun_month_jan)
                2 -> sunDateText += application.getString(R.string.text_sun_month_feb)
                3 -> sunDateText += application.getString(R.string.text_sun_month_mar)
                4 -> sunDateText += application.getString(R.string.text_sun_month_apr)
                5 -> sunDateText += application.getString(R.string.text_sun_month_may)
                6 -> sunDateText += application.getString(R.string.text_sun_month_jun)
                7 -> sunDateText += application.getString(R.string.text_sun_month_jul)
                8 -> sunDateText += application.getString(R.string.text_sun_month_aug)
                9 -> sunDateText += application.getString(R.string.text_sun_month_sep)
                10 -> sunDateText += application.getString(R.string.text_sun_month_oct)
                11 -> sunDateText += application.getString(R.string.text_sun_month_nov)
                12 -> sunDateText += application.getString(R.string.text_sun_month_dec)
                else -> {}
            }
            when (mdate.nextToken().toInt()) {
                1 -> moonDateText += application.getString(R.string.text_moon_month_muhram)
                2 -> moonDateText += application.getString(R.string.text_moon_month_safer)
                3 -> moonDateText += application.getString(R.string.text_moon_month_rabi_aoul)
                4 -> moonDateText += application.getString(R.string.text_moon_month_rabi_aker)
                5 -> moonDateText += application.getString(R.string.text_moon_month_gamada_aoul)
                6 -> moonDateText += application.getString(R.string.text_moon_month_gamada_aker)
                7 -> moonDateText += application.getString(R.string.text_moon_month_rajb)
                8 -> moonDateText += application.getString(R.string.text_moon_month_shaban)
                9 -> moonDateText += application.getString(R.string.text_moon_month_ramadan)
                10 -> moonDateText += application.getString(R.string.text_moon_month_shual)
                11 -> moonDateText += application.getString(R.string.text_moon_month_zo_kada)
                12 -> moonDateText += application.getString(R.string.text_moon_month_zo_haga)
                else -> {}
            }
            sunDateText += " " + sdate.nextToken()
            moonDateText += " " + mdate.nextToken()
            moonDateBanner.set(moonDateText)
            sunDateBanner.set(sunDateText)
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun isDayHasChanged(date: String?): Boolean {
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

    private fun isRamadan(): Boolean {
        try {
            val mdate = StringTokenizer(
                domainPrayer.mDate,
                "."
            )
            mdate.nextToken()
            if (mdate.nextToken().toInt() == 9) {
                return true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun setCalenderPrayersTime() {
        fajerTime = domainPrayer.fajer!!.toCalendar()
        sunriseTime = domainPrayer.sunrise!!.toCalendar()
        duhrTime = domainPrayer.duhr!!.toCalendar()
        asrTime = domainPrayer.asr!!.toCalendar()
        sunsetTime = domainPrayer.maghrib!!.toCalendar()
        ishaTime = domainPrayer.isha!!.toCalendar()
        midNightTime = Calendar.getInstance()
        midNightTime.set(
            Calendar.HOUR_OF_DAY,
            23
        )
        midNightTime.set(
            Calendar.MINUTE,
            59
        )
    }

    /**Update Background pic according to time*/
    private fun updateBackground() {
        if (!isNight()) {
            if (isRamadan()) {
                backgroundImage.set(R.drawable.ramadan_day)
            } else {
                backgroundImage.set(R.drawable.background_day)
            }
            btnSettingsResource.set(R.drawable.round_settings_black)
            Log.i(
                "UI",
                "Day time"
            )
        } else {
            if (isRamadan()) {
                backgroundImage.set(R.drawable.ramadan_night)
            } else {
                backgroundImage.set(R.drawable.background_night)
            }
            btnSettingsResource.set(R.drawable.round_settings_white)
            Log.i(
                "UI",
                "Night time"
            )
        }
    }
}
