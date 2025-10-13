package com.gals.prayertimes.permissions.notification

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.gals.prayertimes.permissions.PermissionHandler
import com.gals.prayertimes.utils.upAPILevel33
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Handles the notification permission (POST_NOTIFICATIONS) for Android 13 and above.
 */
class NotificationPermissionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat
) : PermissionHandler {

    override fun isGranted(): Boolean = if (upAPILevel33) {
        Timber.d("Notification permission granted: ${notificationManagerCompat.areNotificationsEnabled()}")
        notificationManagerCompat.areNotificationsEnabled()
    } else {
        true
    }

    override fun openSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}