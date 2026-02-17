package com.gals.prayertimes.settings.tracker

import com.gals.prayertimes.tracking.Tracker
import javax.inject.Inject

class NotificationSettingsTrackerImpl @Inject constructor(
    private val tracker: Tracker
) : NotificationSettingsTracker {

    override fun submitSettings(params: Map<String, Any>) {
        tracker.logEvent(name = "notification_settings_submitted", params = params)
    }
}