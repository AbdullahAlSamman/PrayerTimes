package com.gals.prayertimes.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gals.prayertimes.model.NotificationType

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    var id: Int = 1,
    @ColumnInfo(defaultValue = "false")
    var notification: Boolean,
    @ColumnInfo(defaultValue = "silent")
    var notificationType: String,
    @ColumnInfo(defaultValue = "false")
    var sunriseNotification : Boolean = false
) {
    companion object {
        val EMPTY: Settings by lazy {
            Settings(
                notification = false,
                notificationType = NotificationType.SILENT.value
            )
        }
    }
}