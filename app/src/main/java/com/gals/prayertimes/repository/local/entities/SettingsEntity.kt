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
    @ColumnInfo(defaultValue = "false")
    var sunriseNotification: Boolean = false
)