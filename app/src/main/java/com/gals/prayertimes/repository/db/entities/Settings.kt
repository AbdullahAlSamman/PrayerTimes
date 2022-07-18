package com.gals.prayertimes.repository.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @field:ColumnInfo(defaultValue = "false")
    var isNotification: Boolean,
    @field:ColumnInfo(defaultValue = "silent")
    var notificationType: String
)