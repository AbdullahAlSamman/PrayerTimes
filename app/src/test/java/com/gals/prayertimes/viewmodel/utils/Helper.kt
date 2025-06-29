package com.gals.prayertimes.viewmodel.utils

import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.model.PrayerNameResponse

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

val testUiPrayer = UiPrayer(
    uiDate = UiDate(
        dayName = anyString,
        moonDate = dateString,
        sunDate = dateString
    ),
    prayers = mapOf(
        PrayerNameResponse.FAJER to "04:00",
        PrayerNameResponse.SUNRISE to "06:00",
        PrayerNameResponse.DUHR to "13:00",
        PrayerNameResponse.ASR to "16:00",
        PrayerNameResponse.MAGHRIB to "20:00",
        PrayerNameResponse.ISHA to "22:00"
    )
)
