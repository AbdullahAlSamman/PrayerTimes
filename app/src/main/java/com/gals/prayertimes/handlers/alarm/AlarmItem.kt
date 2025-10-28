package com.gals.prayertimes.handlers.alarm

import java.time.LocalDateTime

data class AlarmItem (
    val time: Long,
    val prayer: String
)

data class PrayerAlarmItem(
    val time: LocalDateTime,
    val prayer: String,
    val notificationType: String
)