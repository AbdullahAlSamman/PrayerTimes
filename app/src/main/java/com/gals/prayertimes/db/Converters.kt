package com.gals.prayertimes.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

    @TypeConverter
    fun stringToDate(date: String): Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date)

    @TypeConverter
    fun stringToBoolean(bool: String?): Boolean = bool.toBoolean()
}