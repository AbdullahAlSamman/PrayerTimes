package com.gals.prayertimes.permissions.tracker

import com.gals.prayertimes.tracking.Tracker
import javax.inject.Inject

class PermissionTrackerImpl @Inject constructor(
    private val tracker: Tracker
) : PermissionTracker {
    override fun permissionOpen() {
        tracker.logEvent("permission_screen_open", emptyMap())
    }

    override fun permissionsGranted(isAllGranted: Boolean) {
        val params = buildMap {
            put("granted", isAllGranted)
        }
        tracker.logEvent("permissions_granted", params)
    }

    override fun permissionClose() {
        tracker.logEvent("permission_screen_close", emptyMap())
    }
}