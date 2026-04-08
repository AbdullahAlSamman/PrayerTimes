package com.gals.prayertimes.settings.notification.tracker

interface NotificationSettingsTracker {
    fun submitSettings(params: Map<String, Any>)
}