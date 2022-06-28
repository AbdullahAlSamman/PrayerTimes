package com.gals.prayertimes.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.db.entities.Prayer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import kotlin.math.ceil

class UtilsManager(private val utilsContext: Context) {
    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = utilsContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name
                == service.service.className
            ) {
                return true
            }
        }
        return false
    }

    fun convertDate(): String {
        val todayDay = SimpleDateFormat(
            "dd",
            Locale.US
        ).format(Date())
        val todayMonth = SimpleDateFormat(
            "MM",
            Locale.US
        ).format(Date())
        val todayYear = SimpleDateFormat(
            "yyyy",
            Locale.US
        ).format(Date())
        return convert2Decimal(todayDay) + "." + convert2Decimal(todayMonth) + "." + convert2Decimal(todayYear)
    }

    fun setActivityLanguage(lang: String) {
        val res = utilsContext.resources
        val newConfig = Configuration(res.configuration)
        val locale = Locale(lang)
        newConfig.setLocale(locale)
        newConfig.setLayoutDirection(locale)
        res.updateConfiguration(
            newConfig,
            null
        )
    }

    fun isEqualTime(
        currentTime: Calendar,
        nextTime: Calendar
    ): Boolean {
        val bigHours = ceil((currentTime.timeInMillis / (1000 * 60 * 60)).toDouble()).toInt()
        val bigMins = ceil((currentTime.timeInMillis / (1000 * 60) % 60).toDouble()).toInt()
        val smallHours = ceil((nextTime.timeInMillis / (1000 * 60 * 60)).toDouble()).toInt()
        val smallMins = ceil((nextTime.timeInMillis / (1000 * 60) % 60).toDouble()).toInt()
        if (bigHours == smallHours) {
            if (bigMins == smallMins) {
                return true
            }
        }
        return false
    }

    fun difTimes(
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

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = utilsContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    //in Right To Left layout
    val isRTL: Boolean
        get() {
            val config = utilsContext.resources
                .configuration
            return if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                config.setLayoutDirection(Locale("ar"))
                utilsContext.resources
                    .updateConfiguration(
                        config,
                        utilsContext.resources
                            .displayMetrics
                    )
                //in Right To Left layout
                Log.i(
                    "RTL",
                    "The Language is RTL"
                )
                true
            } else {
                Log.i(
                    "RTL",
                    "The Language is LTR"
                )
                false
            }
        }

    fun changeStatusBarColor(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = utilsContext.resources
                .getColor(R.color.ishaViewColor)
        }
    }

    val isScreenSmall: Boolean
        get() {
            val density = utilsContext.resources
                .displayMetrics.density.toDouble()
            return density <= 1.5
        }

    fun printTest(prayer: Prayer) {
        Log.i(
            "Prayer/info",
            """
                ${prayer.objectId}
                ${prayer.sDate}
                ${prayer.mDate}
                ${prayer.fajer}
                ${prayer.sunrise}
                ${prayer.duhr}
                ${prayer.asr}
                ${prayer.maghrib}
                ${prayer.isha}
                
                """.trimIndent()
        )
    }

    fun printTest(prayer: DomainPrayer) {
        Log.i(
            "Prayer/info",
            """
                ${prayer.objectId}
                ${prayer.sDate}
                ${prayer.mDate}
                ${prayer.fajer}
                ${prayer.sunrise}
                ${prayer.duhr}
                ${prayer.asr}
                ${prayer.maghrib}
                ${prayer.isha}
                """.trimIndent()
        )
    }

    @Deprecated("")
    fun generateRandomNotificationId(): Int {
        val rand = Random()
        return rand.nextInt(5000 - 1000 + 1) + 1000
    }

    @Deprecated("")
    fun restartActivity(
        currentActivity: Context,
        activityToStart: Class<out Activity?>?
    ) {
        val intent = Intent(
            currentActivity,
            activityToStart
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        currentActivity.startActivity(intent)
    }

    companion object {
        private val VIBRATION_PATTERN = longArrayOf(
            100,
            200,
            300,
            400,
            500,
            400,
            300,
            200,
            400
        )

        fun convert2Decimal(number: String): String? {
            try {
                val chars = CharArray(number.length)
                for (i in number.indices) {
                    val ch = number[i]
                    if (ch.code in 0x0660..0x0669) {
                        (ch.minus(0x0660 - '0'.code))
                    } else if (ch.code in 0x06f0..0x06F9) {
                        (ch.minus(0x06f0 - '0'.code))
                    }
                    chars[i] = ch
                }
                return String(chars)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}