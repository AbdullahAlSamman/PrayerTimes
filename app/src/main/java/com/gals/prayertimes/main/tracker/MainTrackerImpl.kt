package com.gals.prayertimes.main.tracker

import com.gals.prayertimes.tracking.Tracker
import javax.inject.Inject

class MainTrackerImpl @Inject constructor(
    private val tracker: Tracker
) : MainTracker {
    override fun consent(isGranted: Boolean) {
        val params = buildMap {
            put("granted", isGranted)
        }
        tracker.logEvent("consent_granted", params)
    }

    override fun consentError(code: String, message: String) {
        val params = buildMap {
            put("error", code)
            put("message", message)
        }
        tracker.logEvent("consent_error", params)
    }

    override fun loading() {
        tracker.logEvent("loading_started", emptyMap())
    }

    override fun reload() {
        tracker.logEvent("reloading_started", emptyMap())
    }

    override fun error(error: String) {
        val params = buildMap {
            put("error", error)
        }
        tracker.logEvent("loading_error", params)
    }

    override fun settingsOpen() {
        tracker.logEvent("settings_menu_opened", emptyMap())
    }

    override fun navigationMenuItemOpen(navTarget: String) {
        val params = buildMap {
            put("target", navTarget)
        }
        tracker.logEvent("navigation_menu_item_opened", params)
    }
}