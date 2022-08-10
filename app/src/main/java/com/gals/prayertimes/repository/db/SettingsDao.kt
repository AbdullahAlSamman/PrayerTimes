package com.gals.prayertimes.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gals.prayertimes.repository.db.entities.Settings

/**
 * The interface Prayer dao.
 * defines all the queries as function with sql query to retrive or insert or delete data from data base the implementation with be generated in compile time form Room Library.
 */
@Dao
interface SettingsDao {
    /**
     * get all the settings.
     *
     * @return the Settings
     */
    @get:Query("SELECT * FROM settings LIMIT 1")
    val settings: Settings?

    /**
     * update settings.
     *
     * @return the Settings
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(settings: Settings?)

    /**
     * insert settings.
     *
     * @return the Settings
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: Settings?)

    /**
     * Check if the table has rows
     * @return the Settings
     */
    @Query("SELECT EXISTS(SELECT * FROM settings)")
    fun isExists(): Boolean
}