package com.gals.prayertimes.utils

import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import java.util.Calendar
import java.util.Locale
import kotlin.math.ceil

class UtilsManager(private val utilsContext: Context) {
    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = utilsContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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

    fun calculateDifferenceBetweenTimes(
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

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            utilsContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun changeStatusBarColor(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = utilsContext.resources
            .getColor(R.color.background_color_time_isha)
    }

    fun printTest(prayer: EntityPrayer) {
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
}