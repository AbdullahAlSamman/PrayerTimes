package com.gals.prayertimes.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gals.prayertimes.repository.local.entities.SettingsEntity

/**
 * The interface Prayer dao.
 * @
 */
@Dao
interface SettingsDao {
    /**
     * Get all the settings.
     *
     * @return settings entity
     */
    @Query("SELECT * FROM settings LIMIT 1")
    suspend fun settingsEntity(): SettingsEntity

    /**
     * Insert settings into db.
     *
     * @param settingsEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settingsEntity: SettingsEntity)

    /**
     * Check if the table has rows
     *
     * @return Boolean true if the settings is already saved on db
     */
    @Query("SELECT EXISTS(SELECT * FROM settings)")
    suspend fun isExists(): Boolean
}