package com.gals.prayertimes.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UtilsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
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