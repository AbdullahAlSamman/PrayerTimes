package com.gals.prayertimes.utils

import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.repository.network.model.NetworkPrayer
import java.text.SimpleDateFormat
import java.util.*

fun EntityPrayer.toDomain(): DomainPrayer = DomainPrayer(
    this.objectId,
    this.sDate,
    this.mDate,
    this.fajer,
    this.sunrise,
    this.duhr,
    this.asr,
    this.maghrib,
    this.isha
)

fun NetworkPrayer.toEntity(): EntityPrayer = EntityPrayer(
    objectId = this.objectId,
    sDate = this.sDate,
    mDate = this.mDate,
    fajer = this.fajer,
    sunrise = this.sunrise,
    duhr = this.duhr,
    asr = this.asr,
    maghrib = this.maghrib,
    isha = this.isha
)

fun getTodayDate(): String = SimpleDateFormat(
    "dd.MM.yyyy",
    Locale.US
).format(Date())

fun getTimeNow(): String = SimpleDateFormat(
    "HH:mm",
    Locale.US
).format(Date())

fun String.toCalendar(): Calendar {
    val calendar: Calendar = Calendar.getInstance()
    val time = this.split(":".toRegex()).toTypedArray()
    calendar.set(
        Calendar.HOUR_OF_DAY,
        time[0].trim { it <= ' ' }.toInt()
    )
    calendar.set(
        Calendar.MINUTE,
        time[1].trim { it <= ' ' }.toInt()
    )
    return calendar
}