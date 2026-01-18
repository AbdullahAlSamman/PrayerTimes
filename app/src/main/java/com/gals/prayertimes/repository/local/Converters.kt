package com.gals.prayertimes.repository.local

import androidx.room.TypeConverter
import com.gals.prayertimes.common.NotificationType

object Converters {
    @TypeConverter
    fun fromNotificationType(value: NotificationType): String = value.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = try {
        NotificationType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        NotificationType.SILENT
    }
}