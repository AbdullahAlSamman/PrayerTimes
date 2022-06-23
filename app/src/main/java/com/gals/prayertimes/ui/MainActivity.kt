@file:Suppress("DEPRECATION")

package com.gals.prayertimes.ui

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.db.entities.Prayer.Companion.toDomain
import com.gals.prayertimes.utils.DataManager
import com.gals.prayertimes.utils.UtilsManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    lateinit var db: AppDB
    lateinit var background: ImageView
    lateinit var domainPrayer: DomainPrayer
    lateinit var prayer: EntityPrayer
    lateinit var fajerLay: LinearLayout
    lateinit var sunriseLay: LinearLayout
    lateinit var duhrLay: LinearLayout
    lateinit var asrLay: LinearLayout
    lateinit var maghribLay: LinearLayout
    lateinit var ishaLay: LinearLayout
    lateinit var fajerText: TextView
    lateinit var sunriseText: TextView
    lateinit var duhrText: TextView
    lateinit var asrText: TextView
    lateinit var maghribText: TextView
    lateinit var ishaText: TextView
    lateinit var sDatePanner: TextView
    lateinit var mDatePanner: TextView
    lateinit var dayPanner: TextView
    lateinit var fajerTime: TextView
    lateinit var sunriseTime: TextView
    lateinit var duhrTime: TextView
    lateinit var asrTime: TextView
    lateinit var maghribTime: TextView
    lateinit var ishaTime: TextView
    lateinit var nextPrayerTime: TextView
    lateinit var nextPrayerText: TextView
    lateinit var nextPrayerPanner: TextView
    lateinit var btnSettings: ImageButton
    lateinit var tools: UtilsManager
    lateinit var updateUITimer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**Tools and DB*/
        tools = UtilsManager(baseContext)
        db = getInstance(baseContext)

        /**UI Elements*/
        background = findViewById<View>(R.id.background) as ImageView
        sDatePanner = findViewById<View>(R.id.sDateView) as TextView
        mDatePanner = findViewById<View>(R.id.mDateView) as TextView
        dayPanner = findViewById<View>(R.id.dayView) as TextView
        fajerTime = findViewById<View>(R.id.fajerTimeText) as TextView
        sunriseTime = findViewById<View>(R.id.sunriseTimeText) as TextView
        duhrTime = findViewById<View>(R.id.duhrTimeText) as TextView
        asrTime = findViewById<View>(R.id.asrTimeText) as TextView
        maghribTime = findViewById<View>(R.id.maghribTimeText) as TextView
        ishaTime = findViewById<View>(R.id.ishaTimeText) as TextView
        nextPrayerText = findViewById<View>(R.id.nextPrayerText) as TextView
        nextPrayerTime = findViewById<View>(R.id.nextTimeText) as TextView
        nextPrayerPanner = findViewById<View>(R.id.prayerText) as TextView
        fajerText = findViewById<View>(R.id.fajertext) as TextView
        sunriseText = findViewById<View>(R.id.sunriseText) as TextView
        duhrText = findViewById<View>(R.id.duhrText) as TextView
        asrText = findViewById<View>(R.id.asrText) as TextView
        maghribText = findViewById<View>(R.id.maghribText) as TextView
        ishaText = findViewById<View>(R.id.ishaText) as TextView
        fajerLay = findViewById<View>(R.id.fajerLayout) as LinearLayout
        sunriseLay = findViewById<View>(R.id.sunriseLayout) as LinearLayout
        duhrLay = findViewById<View>(R.id.duhrLayout) as LinearLayout
        asrLay = findViewById<View>(R.id.asrLayout) as LinearLayout
        maghribLay = findViewById<View>(R.id.maghribLayout) as LinearLayout
        ishaLay = findViewById<View>(R.id.ishaLayout) as LinearLayout
        btnSettings = findViewById<View>(R.id.settingButton) as ImageButton
        //TODO: Internet warning

        /**Change Status bar color*/
        tools.changeStatusBarColor(window)
        startUIUpdate()
        changeFontSizes()
        tools.setActivityLanguage("en")
        val settingsActivity = Intent(
            this,
            Menu::class.java
        )
        btnSettings.setOnClickListener { startActivity(settingsActivity) }
        GetPrayerFromDB().execute(baseContext)
    }

    override fun onResume() {
        super.onResume()
        DataManager(
            null,
            this.baseContext,
            true
        ).execute(
            "",
            "",
            ""
        )
    }

    /**Update Background pic according to time*/
    private fun updateBackground() {
        if (!domainPrayer.isNight) {
            if (domainPrayer.isRamadan) {
                background.setImageResource(R.drawable.ramadan_day)
            } else {
                background.setImageResource(R.drawable.background_day)
            }
            btnSettings.setImageResource(R.drawable.round_settings_black)
            Log.i(
                "UI",
                "Day time"
            )
        } else {
            if (domainPrayer.isRamadan) {
                background.setImageResource(R.drawable.ramadan_night)
            } else {
                background.setImageResource(R.drawable.background_night)
            }
            btnSettings.setImageResource(R.drawable.round_settings_white)
            Log.i(
                "UI",
                "Night time"
            )
        }
    }

    /**update the time to next prayer*/
    private fun updateRemainingTime() {
        if (domainPrayer.calculateTimeBetweenPrayers()) {
            if (domainPrayer.isNextAPrayer) {
                nextPrayerTime.text = domainPrayer.remainingPrayerTime
                nextPrayerText.text = domainPrayer.currentPrayerName
                nextPrayerPanner.text = getString(R.string.remmaning_prayer_time)
            } else {
                nextPrayerTime.text = domainPrayer.remainingPrayerTime
                nextPrayerText.text = domainPrayer.currentPrayerName
                nextPrayerPanner.text = getString(R.string.remmaning_time)
            }
            nextPrayerTime.visibility = View.VISIBLE
            nextPrayerText.visibility = View.VISIBLE
            nextPrayerPanner.visibility = View.VISIBLE
        } else {
            nextPrayerTime.visibility = View.INVISIBLE
            nextPrayerText.visibility = View.INVISIBLE
            nextPrayerPanner.visibility = View.INVISIBLE
        }
    }

    /**Set data on the UI Elements*/
    private fun setSettingsUI() {
        try {
            if (domainPrayer.isValid) {
                domainPrayer.init(baseContext)
                domainPrayer.dateText()
                Log.i(
                    "isChangeTheDayTest",
                    "" + domainPrayer.isDayHasChanged(domainPrayer.sDate)
                )
                if (domainPrayer.isDayHasChanged(domainPrayer.sDate)) {
                    updatePrayers()
                }
                updateRemainingTime()
                updateBackground()
                sDatePanner.text = domainPrayer.sFullDate
                mDatePanner.text = domainPrayer.mFullDate
                dayPanner.text = domainPrayer.day
                fajerTime.text = domainPrayer.fajer
                sunriseTime.text = domainPrayer.sunrise
                duhrTime.text = domainPrayer.duhr
                asrTime.text = domainPrayer.asr
                maghribTime.text = domainPrayer.maghrib
                ishaTime.text = domainPrayer.isha
            } else {
                Log.i(
                    "SettingsUI Update",
                    "there is no data at all"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePrayers() {
        /** update the data from server if the date is changed*/
        if (tools.isNetworkAvailable) {
            try {
                DataManager(
                    null,
                    this.baseContext,
                    true
                ).execute(
                    "",
                    "",
                    ""
                )
                prayer = db.prayerDao()?.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
                convertEntityToDomain()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Fonts for small devices and change the size of Squares
     */
    private fun changeFontSizes() {
        if (tools.isScreenSmall) {
            sDatePanner.textSize = 16f
            mDatePanner.textSize = 16f
            dayPanner.textSize = 16f
            nextPrayerPanner.textSize = 18f
            nextPrayerText.textSize = 18f
            nextPrayerTime.textSize = 20f
            fajerText.textSize = 16f
            sunriseText.textSize = 16f
            duhrText.textSize = 16f
            asrText.textSize = 16f
            maghribText.textSize = 16f
            ishaText.textSize = 16f
            fajerTime.textSize = 16f
            sunriseTime.textSize = 16f
            duhrTime.textSize = 16f
            asrTime.textSize = 16f
            maghribTime.textSize = 16f
            ishaTime.textSize = 16f
            val heightParams = fajerLay.layoutParams as LinearLayout.LayoutParams
            heightParams.height = 150
            fajerLay.layoutParams = heightParams
            sunriseLay.layoutParams = heightParams
            duhrLay.layoutParams = heightParams
            asrLay.layoutParams = heightParams
            maghribLay.layoutParams = heightParams
            ishaLay.layoutParams = heightParams
        }
    }

    /**Timer to update the ui*/
    private fun startUIUpdate(time: Int = 25000) {
        val handler = Handler()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        setSettingsUI()
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

    private fun convertEntityToDomain() {
        domainPrayer = prayer.toDomain()
    }

    /**
     * Get data from db.
     */
    inner class GetPrayerFromDB : AsyncTask<Context?, String?, String?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Context?): String? {
            try {
                prayer = db.prayerDao()?.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
                convertEntityToDomain()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (domainPrayer.isValid) {
                startUIUpdate()
            }
        }
    }
}
