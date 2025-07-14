package com.gals.prayertimes.services.alarmmanager

import java.time.LocalDateTime

data class AlarmItem (
    val time: LocalDateTime,
    val prayer: String,
    val notificationType: String
)