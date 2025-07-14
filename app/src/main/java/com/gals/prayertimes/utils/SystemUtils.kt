package com.gals.prayertimes.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemUtils @Inject constructor(
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
            NotificationType.FULL -> (URI_DEFAULT_PATH + R.raw.fullathan).toUri()
            NotificationType.HALF -> (URI_DEFAULT_PATH + R.raw.halfathan).toUri()
            else -> Uri.EMPTY
        }

    fun hasNotificationPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    companion object Companion {
        private const val URI_DEFAULT_PATH = "android.resource://com.gals.prayertimes/"
    }
}