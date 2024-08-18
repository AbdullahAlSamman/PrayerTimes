package com.gals.prayertimes.viewmodel.utils

import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerName

const val errorMessage = "errorMessage"
const val anyString = "anyString"
const val dateString = "dateString"

val testPrayerEntity = PrayerEntity(
    objectId = "id",
    sDate = "01.02.2023",
    mDate = "01.03.1443",
    fajer = "04:00",
    sunrise = "06:00",
    duhr = "13:00",
    asr = "16:00",
    maghrib = "20:00",
    isha = "22:00"
)

val testNextPrayerConfig = NextPrayerConfig(
    nextPrayerBanner = "asr",
    nextPrayerName = "Asr",
    nextPrayerTime = "16:00"
)

val testPrayer = Prayer(
    uiDate = UiDate(
        dayName = anyString,
        moonDate = dateString,
        sunDate = dateString
    ),
    prayers = mapOf(
        PrayerName.FAJER to "04:00",
        PrayerName.SUNRISE to "06:00",
        PrayerName.DUHR to "13:00",
        PrayerName.ASR to "16:00",
        PrayerName.MAGHRIB to "20:00",
        PrayerName.ISHA to "22:00"
    )
)
