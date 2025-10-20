package com.gals.prayertimes.viewmodel.utils

import com.gals.prayertimes.common.NextPrayerConfig
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiPrayer
import com.gals.prayertimes.repository.local.entities.PrayerEntity

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
        UiPrayerName.FAJER to "04:00",
        UiPrayerName.SUNRISE to "06:00",
        UiPrayerName.DUHR to "13:00",
        UiPrayerName.ASR to "16:00",
        UiPrayerName.MAGHRIB to "20:00",
        UiPrayerName.ISHA to "22:00"
    )
)
