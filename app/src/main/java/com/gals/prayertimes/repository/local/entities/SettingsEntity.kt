package com.gals.prayertimes.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    var id: Int = 1,
    @ColumnInfo(defaultValue = "false")
    var notification: Boolean,
    @ColumnInfo(defaultValue = "SILENT")
    var notificationType: NotificationType,
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
) {
    companion object {
        fun SettingsEntity.toPrayerNotification(): Map<PrayerName, Boolean> =
            PrayerName.entries.associateWith {
                when (it) {
                    PrayerName.FAJER -> fajerNotification
                    PrayerName.SUNRISE -> sunriseNotification
                    PrayerName.DUHR -> duhrNotification
                    PrayerName.ASR -> asrNotification
                    PrayerName.MAGRIB -> maghribNotification
                    PrayerName.ISHA -> ishaNotification
                }
            }
    }
}