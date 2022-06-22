package com.gals.prayertimes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
class Settings(
    @field:ColumnInfo(defaultValue = "false")
    var isNotification: Boolean,
    @field:ColumnInfo(defaultValue = "silent")
    var notificationType: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}