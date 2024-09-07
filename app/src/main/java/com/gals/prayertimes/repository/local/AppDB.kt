package com.gals.prayertimes.repository.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity

@Database(
    entities = [PrayerEntity::class, SettingsEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    /**
     * Prayers dao.
     * implementation will be generated in compile time from Room Library.
     *
     * @return the Prayer dao
     */
    abstract val prayerDao: PrayerDao

    /**
     * Settings dao.
     * implementation will be generated in compile time from Room Library.
     *
     * @return the Settings dao
     */
    abstract val settingsDao: SettingsDao

    companion object {
        const val DB_NAME = "prayers-db"
    }
}