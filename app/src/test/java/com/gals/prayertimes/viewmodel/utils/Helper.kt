package com.gals.prayertimes.viewmodel.utils

import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.repository.local.entities.PrayerEntity

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