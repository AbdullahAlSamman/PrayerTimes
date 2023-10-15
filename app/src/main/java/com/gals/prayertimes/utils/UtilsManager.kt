package com.gals.prayertimes.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.view.Window
import android.view.WindowManager
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class UtilsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun startService(serviceClass: Class<*>) {
        context.startService(Intent(context, serviceClass))
    }

    fun stopService(serviceClass: Class<*>) {
        context.stopService(Intent(context, serviceClass))
    }

    fun restartService(serviceClass: Class<*>) {
        stopService(serviceClass)
        startService(serviceClass)
    }

    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getSoundUri(notificationType: NotificationType): Uri =
        when (notificationType) {
            NotificationType.FULL -> Uri.parse(URI_DEFAULT_PATH + R.raw.fullathan)
            NotificationType.HALF -> Uri.parse(URI_DEFAULT_PATH + R.raw.halfathan)
            else -> Uri.EMPTY
        }

    companion object {
        private const val URI_DEFAULT_PATH = "android.resource://com.gals.prayertimes/"
    }
}