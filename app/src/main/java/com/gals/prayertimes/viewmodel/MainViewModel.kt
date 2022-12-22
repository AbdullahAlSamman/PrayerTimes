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
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.config.NextPrayerInfoConfig
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.localdatasource.entities.Prayer.Companion.isValid
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.getDayName
import com.gals.prayertimes.utils.getMoonMonth
import com.gals.prayertimes.utils.getSunMonth
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toDomain
import com.gals.prayertimes.utils.toTimePrayer
import com.gals.prayertimes.view.Menu
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.StringTokenizer
import java.util.Timer
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val application: Context,
    private val repository: Repository
) : ViewModel() {
    @Inject
    lateinit var tools: UtilsManager

    private val calculation: PrayerCalculation = PrayerCalculation(application)

    private var domainPrayer: DomainPrayer = DomainPrayer.EMPTY
    private lateinit var currentPrayer: TimePrayer
    private lateinit var timerHandler: Handler
    var backgroundImage: ObservableInt = ObservableInt()
    var btnSettingsResource: ObservableInt = ObservableInt()
    var sunDateBanner: ObservableField<String> = ObservableField()
    var moonDateBanner: ObservableField<String> = ObservableField()
    var nextPrayerTime: ObservableField<String> = ObservableField()
    var nextPrayerBanner: ObservableField<String> = ObservableField()
    var nextPrayerName: ObservableField<String> = ObservableField()
    var viewDayName: ObservableField<String> = ObservableField()
    var viewFajerTime: ObservableField<String> = ObservableField()
    var viewSunriseTime: ObservableField<String> = ObservableField()
    var viewDuhrTime: ObservableField<String> = ObservableField()
    var viewAsrTime: ObservableField<String> = ObservableField()
    var viewMaghribTime: ObservableField<String> = ObservableField()
    var viewIshaTime: ObservableField<String> = ObservableField()

    init {
        timerHandler = Handler(Looper.getMainLooper())
    }

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
    private fun startDateUpdate(time: Long = 25000) {
        timerHandler.post(object : Runnable {
            override fun run() {
                updateDateUIObservables()
                timerHandler.postDelayed(this, time)
            }
        })
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
                            updateViewObservableValues(domainPrayer)
                        }
                    }
                }
            }
        }
    }

    /**update the time to next prayer*/
    private fun updateRemainingTime() {
        currentPrayer = domainPrayer.toTimePrayer()
        updateNextPrayerInfo(calculation.calculateNextPrayerInfo(currentPrayer))
    }

    /** Build text for dates*/
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

    /**Update Background pic according to time*/
    private fun updateBackground() {
        if (!calculation.isNight(currentPrayer)) {
            when (calculation.isRamadan(domainPrayer.mDate)) {
                true -> backgroundImage.set(R.drawable.ramadan_day)
                false -> backgroundImage.set(R.drawable.background_day)
            }
            btnSettingsResource.set(R.drawable.round_settings_black)
            Log.i("UI", "Day time")
        } else {
            when (calculation.isRamadan(domainPrayer.mDate)) {
                true -> backgroundImage.set(R.drawable.ramadan_night)
                false -> backgroundImage.set(R.drawable.background_night)
            }
            btnSettingsResource.set(R.drawable.round_settings_white)
            Log.i("UI", "Night time")
        }
    }

    private fun updateNextPrayerInfo(nextPrayerInfo: NextPrayerInfoConfig) {
        nextPrayerTime.set(nextPrayerInfo.nextPrayerTime)
        nextPrayerBanner.set(nextPrayerInfo.nextPrayerBannerText)
        nextPrayerName.set(nextPrayerInfo.nextPrayerNameText)
    }
}