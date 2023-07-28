package com.gals.prayertimes.repository.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayers")
data class PrayerEntity(
    @PrimaryKey val objectId: String = "",
    val sDate: String = "",
    val mDate: String = "",
    var fajer: String = "",
    var sunrise: String = "",
    var duhr: String = "",
    var asr: String = "",
    var maghrib: String = "",
    var isha: String = ""
)