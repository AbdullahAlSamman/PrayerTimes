package com.gals.prayertimes.permissions.manager.battery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.gals.prayertimes.permissions.manager.PermissionHandler
import com.gals.prayertimes.utils.upAPILevel23
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Handles ignoring battery optimizations to allow background work.
 */
class BatteryOptimizationPermissionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val powerManager: PowerManager
) : PermissionHandler {

    override fun isGranted(): Boolean =
        if (upAPILevel23) {
            Timber.d("Battery permission granted: ${ powerManager.isIgnoringBatteryOptimizations(context.packageName)}")
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } else {
            true
        }

    override fun requestPermission() {
        if (upAPILevel23) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}
