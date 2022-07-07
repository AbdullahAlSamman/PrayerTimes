package com.gals.prayertimes.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.entities.Prayer.Companion.toDomain
import com.gals.prayertimes.utils.DataManager
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.view.Menu
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.StringTokenizer
import java.util.Timer
import java.util.TimerTask

class MainViewModel(
    private val application: Context,
    private val database: AppDB
) : ViewModel() {
    private lateinit var updateUITimer: Timer
    lateinit var prayer: EntityPrayer
    lateinit var domainPrayer: DomainPrayer
    var backgroundImage: ObservableInt = ObservableInt()
    var btnSettingsResource: ObservableInt = ObservableInt()
    var sunDateBanner: ObservableField<String> = ObservableField()
    var moonDateBanner: ObservableField<String> = ObservableField()
    var nextPrayerTime: ObservableField<String> = ObservableField()
    var nextPrayerText: ObservableField<String> = ObservableField()
    var nextPrayerBanner: ObservableField<String> = ObservableField()
    var currentPrayerName: ObservableField<String> = ObservableField()
    var viewFajerTime: ObservableField<String> = ObservableField()
    var viewSunriseTime: ObservableField<String> = ObservableField()
    var viewDuhrTime: ObservableField<String> = ObservableField()
    var viewAsrTime: ObservableField<String> = ObservableField()
    var viewMaghribTime: ObservableField<String> = ObservableField()
    var viewIshaTime: ObservableField<String> = ObservableField()
    var prayerRemainingTimeSectionVisibility: ObservableBoolean = ObservableBoolean()
    var day:ObservableField<String> = ObservableField()
    private lateinit var remainingPrayerTime: String
    lateinit var sFullDate: String
    lateinit var mFullDate: String
    private lateinit var fajerTime: Calendar
    private var sunriseTime: Calendar = Calendar.getInstance()
    private lateinit var duhrTime: Calendar
    private lateinit var asrTime: Calendar
    private lateinit var sunsetTime: Calendar
    private lateinit var ishaTime: Calendar
    private var midNightTime: Calendar = Calendar.getInstance()
    private lateinit var currentTime: Calendar
    private lateinit var ssTime: Array<String>
    private lateinit var frTime: Array<String>
    private lateinit var srTime: Array<String>
    private lateinit var duTime: Array<String>
    private lateinit var asTime: Array<String>
    private lateinit var isTime: Array<String>
    private lateinit var cTime: Array<String>
    var tools: UtilsManager
    var fullAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.fullathan)
    var halfAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.halfathan)
    var silentAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.silent)
    var notification: Boolean? = null
    var notificationType: String? = null

    init {
        tools = UtilsManager(application)
    }

    /**Timer to update the ui*/
    internal fun startDateUpdate(time: Int = 25000) {
        val handler = Handler()
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

    /**Set data on the UI Elements*/
    internal fun updateDateUIObservables() {
        try {
            buildDateTexts()
            Log.i(
                "isChangeTheDayTest",
                "" + isDayHasChanged(domainPrayer.sDate)
            )
            if (isDayHasChanged(domainPrayer.sDate)) {
                updatePrayers()
            }
            updateRemainingTime()
            updateBackground()
            sunDateBanner.set(sFullDate)
            moonDateBanner.set(mFullDate)
        } catch (e: Exception) {
            Log.i(
                "SettingsUI Update",
                "there is no data at all"
            )
            e.printStackTrace()
        }
    }

    private fun updatePrayers() {
        /** update the data from server if the date is changed*/
        if (tools?.isNetworkAvailable == true) {
            try {
                DataManager(
                    null,
                    application.applicationContext,
                    true
                ).execute(
                    "",
                    "",
                    ""
                )
                prayer = database.prayerDao()?.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
                domainPrayer = prayer.toDomain()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**update the time to next prayer*/
    private fun updateRemainingTime() {
        if (calculateTimeBetweenPrayers()) {
            if (isNextAPrayer) {
                nextPrayerTime.set(remainingPrayerTime)
                nextPrayerText.set(currentPrayerName.toString())
                nextPrayerBanner.set(application.getString(R.string.text_remaining_prayer_time))
            } else {
                nextPrayerTime.set(remainingPrayerTime)
                nextPrayerText.set(currentPrayerName.toString())
                nextPrayerBanner.set(application.getString(R.string.text_remaining_time))
            }
            prayerRemainingTimeSectionVisibility.set(true)
        } else {
            prayerRemainingTimeSectionVisibility.set(false)
        }
    }

    /*TODO: move to data manger*/
    fun getTodayPrayers(dateToday: String): Boolean {
        var result = false
        try {
            val url = URL("http://prayers.esy.es/api/prayers/$dateToday")
            val httpConn = url.openConnection() as HttpURLConnection
            httpConn.requestMethod = "GET"
            val inputStream = httpConn.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val line = bufferedReader.readLine()
            httpConn.disconnect()
            val json = JSONArray(line)
            domainPrayer =
                DomainPrayer(
                    objectId = json.getJSONObject(0).getString("id"),
                    sDate = json.getJSONObject(0).getString("sDate"),
                    mDate = json.getJSONObject(0).getString("mDate"),
                    fajer = json.getJSONObject(0).getString("fajer"),
                    sunrise = json.getJSONObject(0).getString("sunrise"),
                    duhr = json.getJSONObject(0).getString("duhr"),
                    asr = json.getJSONObject(0).getString("asr"),
                    maghrib = json.getJSONObject(0).getString("maghrib"),
                    isha = json.getJSONObject(0).getString("isha")
                )
            result = true
        } catch (e1: java.lang.Exception) {
            e1.printStackTrace()
        }
        return result
    }

    private val isNight: Boolean
        get() {
            try {
                currentTime = Calendar.getInstance()
                if (domainPrayer.sunrise != null || domainPrayer.maghrib != null) {
                    cTime = SimpleDateFormat(
                        "HH:mm",
                        Locale.US
                    ).format(Date())
                        .split(":".toRegex()).toTypedArray()
                    currentTime.set(
                        Calendar.HOUR_OF_DAY,
                        cTime[0].trim { it <= ' ' }.toInt()
                    )
                    currentTime.set(
                        Calendar.MINUTE,
                        cTime[1].trim { it <= ' ' }.toInt()
                    )
                    Log.i(
                        "Prayer/Current Time ",
                        cTime[0] + ":" + cTime[1]
                    )
                    return !(currentTime.before(sunsetTime) && currentTime.after(sunriseTime))
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return false
        }
    private val isNextSunrise: Boolean
        get() = try {
            currentTime = Calendar.getInstance()
            cTime = SimpleDateFormat(
                "HH:mm",
                Locale.US
            ).format(Date())
                .split(":".toRegex()).toTypedArray()
            currentTime.set(
                Calendar.HOUR_OF_DAY,
                cTime[0].trim { it <= ' ' }.toInt()
            )
            currentTime.set(
                Calendar.MINUTE,
                cTime[1].trim { it <= ' ' }.toInt()
            )
            currentTime.before(sunriseTime) && currentTime.after(fajerTime)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            false
        }
    private val isNextAPrayer: Boolean = !(isNextMidnight() || isNextSunrise)

    private fun isNextMidnight(): Boolean {
        try {
            currentTime = Calendar.getInstance()
            cTime = SimpleDateFormat(
                "HH:mm",
                Locale.US
            ).format(Date())
                .split(":".toRegex()).toTypedArray()
            currentTime.set(
                Calendar.HOUR_OF_DAY,
                cTime[0].trim { it <= ' ' }.toInt()
            )
            currentTime.set(
                Calendar.MINUTE,
                cTime[1].trim { it <= ' ' }.toInt()
            )
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            false
        }
        return currentTime.before(midNightTime) && currentTime.after(ishaTime)
    }

    private fun calculateTimeBetweenPrayers(): Boolean {
        return try {
            currentTime = Calendar.getInstance()
            cTime = SimpleDateFormat(
                "HH:mm",
                Locale.US
            ).format(Date())
                .split(":".toRegex()).toTypedArray()
            currentTime.set(
                Calendar.HOUR_OF_DAY,
                cTime[0].trim { it <= ' ' }.toInt()
            )
            currentTime.set(
                Calendar.MINUTE,
                cTime[1].trim { it <= ' ' }.toInt()
            )

            // to update calender instances if the times are updated
            setCalenderPrayersTime()
            if (currentTime.before(fajerTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    fajerTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_fajer))
                true
            } else if (currentTime.before(sunriseTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    sunriseTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_day_sunrise))
                true
            } else if (currentTime.before(duhrTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    duhrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_duhr))
                true
            } else if (currentTime.before(asrTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    asrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_asr))
                true
            } else if (currentTime.before(sunsetTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    sunsetTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_maghrib))
                true
            } else if (currentTime.before(ishaTime)) {
                remainingPrayerTime = tools!!.difTimes(
                    ishaTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_isha))
                true
            } else if (currentTime.before(midNightTime) || currentTime == midNightTime) {
                remainingPrayerTime = tools!!.difTimes(
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
    }

    fun notificationCheckPrayers(): Boolean {
        return try {
            cTime = SimpleDateFormat(
                "HH:mm",
                Locale.US
            ).format(Date())
                .split(":".toRegex()).toTypedArray()
            currentTime = Calendar.getInstance()
            currentTime.set(
                Calendar.HOUR_OF_DAY,
                cTime[0].trim { it <= ' ' }.toInt()
            )
            currentTime.set(
                Calendar.MINUTE,
                cTime[1].trim { it <= ' ' }.toInt()
            )
            Log.i(
                "Prayer/Current Time ",
                cTime[0] + ":" + cTime[1]
            )

            //update Calender Instances if the values changes
            setCalenderPrayersTime()
            if (tools!!.isEqualTime(
                    currentTime,
                    fajerTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/Fajer Time ",
                    frTime[0] + ":" + frTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    fajerTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_fajer))
                true
            } else if (tools!!.isEqualTime(
                    currentTime,
                    sunriseTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/Sunrise Time ",
                    srTime[0] + ":" + srTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    sunriseTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_sunrise))
                true
            } else if (tools!!.isEqualTime(
                    currentTime,
                    duhrTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/duhr Time ",
                    duTime[0] + ":" + duTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    duhrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_duhr))
                true
            } else if (tools!!.isEqualTime(
                    currentTime,
                    asrTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/Asr Time ",
                    asTime[0] + ":" + asTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    asrTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_asr))
                true
            } else if (tools!!.isEqualTime(
                    currentTime,
                    sunsetTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/Sunset Time ",
                    ssTime[0] + ":" + ssTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    sunsetTime,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_maghrib))
                true
            } else if (tools!!.isEqualTime(
                    currentTime,
                    ishaTime
                )
            ) {
                Log.i(
                    "Prayer/Current Time ",
                    cTime[0] + ":" + cTime[1]
                )
                Log.i(
                    "Prayer/Isha Time ",
                    cTime[0] + ":" + cTime[1]
                )
                remainingPrayerTime = tools!!.difTimes(
                    ishaTime!!,
                    currentTime
                )
                currentPrayerName.set(application.getString(R.string.text_prayer_isha))
                true
            } else {
                currentPrayerName.set("")
                remainingPrayerTime = ""
                Log.i(
                    "Prayer/Equals Check ",
                    "False"
                )
                false
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            false
        }
    }

    private fun buildDateTexts(): Boolean {
        return try {
            var day: String? = " "
            var ssdate = " "
            var mmdate = " "
            val calendar = Calendar.getInstance()
            when (calendar[Calendar.DAY_OF_WEEK]) {
                Calendar.SATURDAY -> day = application.getString(R.string.text_day_sat)
                Calendar.SUNDAY -> day = application.getString(R.string.text_day_sun)
                Calendar.MONDAY -> day = application.getString(R.string.text_day_mon)
                Calendar.TUESDAY -> day = application.getString(R.string.text_day_tue)
                Calendar.WEDNESDAY -> day = application.getString(R.string.text_day_web)
                Calendar.THURSDAY -> day = application.getString(R.string.text_day_thr)
                Calendar.FRIDAY -> day = application.getString(R.string.text_day_fri)
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
            ssdate = sdate.nextToken() + " "
            mmdate = mdate.nextToken() + " "
            when (sdate.nextToken().toInt()) {
                1 -> ssdate += application.getString(R.string.text_sun_month_jan)
                2 -> ssdate += application.getString(R.string.text_sun_month_feb)
                3 -> ssdate += application.getString(R.string.text_sun_month_mar)
                4 -> ssdate += application.getString(R.string.text_sun_month_apr)
                5 -> ssdate += application.getString(R.string.text_sun_month_may)
                6 -> ssdate += application.getString(R.string.text_sun_month_jun)
                7 -> ssdate += application.getString(R.string.text_sun_month_jul)
                8 -> ssdate += application.getString(R.string.text_sun_month_aug)
                9 -> ssdate += application.getString(R.string.text_sun_month_sep)
                10 -> ssdate += application.getString(R.string.text_sun_month_oct)
                11 -> ssdate += application.getString(R.string.text_sun_month_nov)
                12 -> ssdate += application.getString(R.string.text_sun_month_dec)
                else -> {}
            }
            when (mdate.nextToken().toInt()) {
                1 -> mmdate += application.getString(R.string.text_moon_month_muhram)
                2 -> mmdate += application.getString(R.string.text_moon_month_safer)
                3 -> mmdate += application.getString(R.string.text_moon_month_rabi_aoul)
                4 -> mmdate += application.getString(R.string.text_moon_month_rabi_aker)
                5 -> mmdate += application.getString(R.string.text_moon_month_gamada_aoul)
                6 -> mmdate += application.getString(R.string.text_moon_month_gamada_aker)
                7 -> mmdate += application.getString(R.string.text_moon_month_rajb)
                8 -> mmdate += application.getString(R.string.text_moon_month_shaban)
                9 -> mmdate += application.getString(R.string.text_moon_month_ramadan)
                10 -> mmdate += application.getString(R.string.text_moon_month_shual)
                11 -> mmdate += application.getString(R.string.text_moon_month_zo_kada)
                12 -> mmdate += application.getString(R.string.text_moon_month_zo_haga)
                else -> {}
            }
            ssdate = ssdate + " " + sdate.nextToken()
            mmdate = mmdate + " " + mdate.nextToken()
            mFullDate = mmdate
            sFullDate = ssdate
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

    private val isRamadan: Boolean
        get() {
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
        fajerTime = Calendar.getInstance()
        sunriseTime = Calendar.getInstance()
        duhrTime = Calendar.getInstance()
        asrTime = Calendar.getInstance()
        sunsetTime = Calendar.getInstance()
        ishaTime = Calendar.getInstance()
        midNightTime = Calendar.getInstance()
        ssTime = domainPrayer.maghrib!!.split(":".toRegex()).toTypedArray()
        frTime = domainPrayer.fajer!!.split(":".toRegex()).toTypedArray()
        srTime = domainPrayer.sunrise!!.split(":".toRegex()).toTypedArray()
        duTime = domainPrayer.duhr!!.split(":".toRegex()).toTypedArray()
        asTime = domainPrayer.asr!!.split(":".toRegex()).toTypedArray()
        isTime = domainPrayer.isha!!.split(":".toRegex()).toTypedArray()
        fajerTime.set(
            Calendar.HOUR_OF_DAY,
            frTime[0].trim { it <= ' ' }.toInt()
        )
        fajerTime.set(
            Calendar.MINUTE,
            frTime[1].trim { it <= ' ' }.toInt()
        )
        sunriseTime.set(
            Calendar.HOUR_OF_DAY,
            srTime[0].trim { it <= ' ' }.toInt()
        )
        sunriseTime.set(
            Calendar.MINUTE,
            srTime[1].trim { it <= ' ' }.toInt()
        )
        duhrTime.set(
            Calendar.HOUR_OF_DAY,
            duTime[0].trim { it <= ' ' }.toInt()
        )
        duhrTime.set(
            Calendar.MINUTE,
            duTime[1].trim { it <= ' ' }.toInt()
        )
        asrTime.set(
            Calendar.HOUR_OF_DAY,
            asTime[0].trim { it <= ' ' }.toInt()
        )
        asrTime.set(
            Calendar.MINUTE,
            asTime[1].trim { it <= ' ' }.toInt()
        )
        sunsetTime.set(
            Calendar.HOUR_OF_DAY,
            ssTime[0].trim { it <= ' ' }.toInt()
        )
        sunsetTime.set(
            Calendar.MINUTE,
            ssTime[1].trim { it <= ' ' }.toInt()
        )
        ishaTime.set(
            Calendar.HOUR_OF_DAY,
            isTime[0].trim { it <= ' ' }.toInt()
        )
        ishaTime.set(
            Calendar.MINUTE,
            isTime[1].trim { it <= ' ' }.toInt()
        )
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
        if (!isNight) {
            if (isRamadan) {
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
            if (isRamadan) {
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

    fun updateViewObservableValues(prayer: DomainPrayer) {
        viewFajerTime.set(prayer.fajer)
        viewSunriseTime.set(prayer.sunrise)
        viewDuhrTime.set(prayer.duhr)
        viewAsrTime.set(prayer.asr)
        viewMaghribTime.set(prayer.maghrib)
        viewIshaTime.set(prayer.isha)
    }

    fun navigateToSettings() {
        application.startActivity(
            Intent(
                application,
                Menu::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
