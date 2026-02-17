package com.gals.prayertimes.settings.tracker

interface NotificationSettingsTracker {
    fun submitSettings(params: Map<String, Any>)
}