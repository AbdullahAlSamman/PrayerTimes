package com.gals.prayertimes.permissions.battery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.gals.prayertimes.permissions.PermissionHandler
import com.gals.prayertimes.utils.upAPILevel23
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Handles ignoring battery optimizations to allow background work.
 */
class BatteryOptimizationPermissionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val powerManager: PowerManager
) : PermissionHandler {

    override fun isGranted(): Boolean {
        return if (upAPILevel23) {
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } else {
            true // Not applicable on older versions
        }
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
