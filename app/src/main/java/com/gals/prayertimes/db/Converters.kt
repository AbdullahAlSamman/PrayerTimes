package com.gals.prayertimes.db

import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun stringToDate(date: String?): Date? {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    @TypeConverter
    fun stringToBoolean(bool: String?): Boolean {
        return java.lang.Boolean.parseBoolean(bool)
    }
}