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
        when {
            currentTime.before(fajerTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    fajerTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_fajer))
                true
            }
            currentTime.before(sunriseTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    sunriseTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_day_sunrise))
                true
            }
            currentTime.before(duhrTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    duhrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_duhr))
                true
            }
            currentTime.before(asrTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    asrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_asr))
                true
            }
            currentTime.before(sunsetTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    sunsetTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_maghrib))
                true
            }
            currentTime.before(ishaTime) -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    ishaTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_isha))
                true
            }
            currentTime.before(midNightTime) || currentTime == midNightTime -> {
                remainingPrayerTime = tools.calculateDifferenceBetweenTimes(
                    midNightTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_midnight_time_title))
                true
            }
            else -> false
        }
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        Log.e(
            "Calc2",
            "The value is False"
        )
        false
    }

    private fun buildDateTexts(): Boolean =
        try {
            lateinit var sunDateText: String
            lateinit var moonDateText: String
            val calendar = Calendar.getInstance()
            viewDayName.set(application.getString(getDayName(calendar[Calendar.DAY_OF_WEEK])))

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

            sunDateText += application.getString(getSunMonth(sdate.nextToken().toInt()))
            moonDateText += application.getString(getMoonMonth(mdate.nextToken().toInt()))

            sunDateText += " " + sdate.nextToken()
            moonDateText += " " + mdate.nextToken()

            moonDateBanner.set(moonDateText)
            sunDateBanner.set(sunDateText)
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
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
            if (mdate.nextToken().toInt() == MONTH_RAMADAN_NUMBER) {
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
            when (isRamadan()) {
                true -> backgroundImage.set(R.drawable.ramadan_day)
                false -> backgroundImage.set(R.drawable.background_day)
            }
            btnSettingsResource.set(R.drawable.round_settings_black)
            Log.i("UI", "Day time")
        } else {
            when (isRamadan()) {
                true -> backgroundImage.set(R.drawable.ramadan_night)
                false -> backgroundImage.set(R.drawable.background_night)
            }
            btnSettingsResource.set(R.drawable.round_settings_white)
            Log.i("UI", "Night time")
        }
    }

    companion object {
        private const val MONTH_RAMADAN_NUMBER = 9
    }
}