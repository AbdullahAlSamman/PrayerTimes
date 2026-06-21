package com.gals.prayertimes.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayers")
data class PrayerEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    val objectId: String = "",
    @ColumnInfo(defaultValue = "")
    val sDate: String = "",
    @ColumnInfo(defaultValue = "")
    val mDate: String = "",
    @ColumnInfo(defaultValue = "")
    var fajer: String = "",
    @ColumnInfo(defaultValue = "")
    var sunrise: String = "",
    @ColumnInfo(defaultValue = "")
    var duhr: String = "",
    @ColumnInfo(defaultValue = "")
    var asr: String = "",
    @ColumnInfo(defaultValue = "")
    var maghrib: String = "",
    @ColumnInfo(defaultValue = "")
    var isha: String = ""
)
