package com.gals.prayertimes.settings.calendar.tracker

import com.gals.prayertimes.tracking.Tracker
import javax.inject.Inject

class PrayerCalendarTrackerImpl @Inject constructor(
    private val tracker: Tracker
) : PrayerCalendarTracker {
    override fun selectedDate(date: String) {
        tracker.logEvent(
            name = "prayer_calendar_selected_date",
            params = mapOf("date" to date)
        )
    }
}