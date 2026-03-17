package com.gals.prayertimes.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Calendar.setHoursMinutes(hours: Int, minutes: Int): Calendar {
    this.set(Calendar.HOUR_OF_DAY, hours)
    this.set(Calendar.MINUTE, minutes)
    return this
}

fun todayDate(): String = SimpleDateFormat(
    "dd.MM.yyyy",
    Locale.US
).format(Date())

fun timeNow(): String = SimpleDateFormat(
    "HH:mm",
    Locale.US
).format(Date())

fun timeNowInMilliseconds(): Long = LocalDate.now()
    .atStartOfDay(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()