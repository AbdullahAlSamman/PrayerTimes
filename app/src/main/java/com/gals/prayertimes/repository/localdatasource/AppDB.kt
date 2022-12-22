package com.gals.prayertimes.repository.localdatasource

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gals.prayertimes.repository.localdatasource.entities.Prayer
import com.gals.prayertimes.repository.localdatasource.entities.Settings

@Database(
    entities = [Prayer::class, Settings::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
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
        private var instance: AppDB? = null

        /**
         * Gets instance.
         * Singleton implementation to forbid creating more than one data base connection.
         *
         * @param context the context of current activity.
         * @return the instance
         */
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    DB_NAME
                ).build()
            }
            return instance as AppDB
        }
    }
}