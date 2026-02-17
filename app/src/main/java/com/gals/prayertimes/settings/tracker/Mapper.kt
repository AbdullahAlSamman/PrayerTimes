package com.gals.prayertimes.settings.tracker

import com.gals.prayertimes.repository.local.entities.SettingsEntity

internal fun SettingsEntity.toTrackingParameters(): Map<String, Any> = buildMap {
    put("notification", notification)
    put("notification_type", notificationType.name)
    put("fajer", fajerNotification)
    put("sunrise", sunriseNotification)
    put("duhr", duhrNotification)
    put("asr", asrNotification)
    put("maghrib", maghribNotification)
    put("isha", ishaNotification)
}