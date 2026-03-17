package com.gals.prayertimes.viewmodel.utils

import com.gals.prayertimes.common.NextPrayerConfig
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiPrayer
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.google.common.collect.ImmutableList

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
    prayers = ImmutableList.of(
        UiPrayerEntry(UiPrayerName.FAJER, "04:00"),
        UiPrayerEntry(UiPrayerName.SUNRISE, "06:00"),
        UiPrayerEntry(UiPrayerName.DUHR, "13:00"),
        UiPrayerEntry(UiPrayerName.ASR, "16:00"),
        UiPrayerEntry(UiPrayerName.MAGHRIB, "20:00"),
        UiPrayerEntry(UiPrayerName.ISHA, "22:00")
    )
)
