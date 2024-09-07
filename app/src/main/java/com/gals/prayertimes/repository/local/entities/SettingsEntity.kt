package com.gals.prayertimes.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    var id: Int = 1,
    @ColumnInfo(defaultValue = "false")
    var notification: Boolean,
    @ColumnInfo(defaultValue = "silent")
    var notificationType: String,
    @ColumnInfo(defaultValue = "true")
    var fajerNotification: Boolean = true,
    @ColumnInfo(defaultValue = "false")
    var sunriseNotification: Boolean = false,
    @ColumnInfo(defaultValue = "true")
    var duhrNotification: Boolean = true,
    @ColumnInfo(defaultValue = "true")
    var asrNotification: Boolean = true,
    @ColumnInfo(defaultValue = "true")
    var maghribNotification: Boolean = true,
    @ColumnInfo(defaultValue = "true")
    var ishaNotification: Boolean = true
)