package com.gals.prayertimes.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gals.prayertimes.model.Settings;

/**
 * The interface Prayer dao.
 * defines all the queries as function with sql query to retrive or insert or delete data from data base the implementation with be generated in compile time form Room Library.
 */
@Dao
public interface SettingsDao {

    /**
     * get all the settings.
     *
     * @return the Settings
     */
    @Query("SELECT * FROM settings LIMIT 1")
    Settings getSettings();

    /**
     * update settings.
     *
     * @return the Settings
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Settings settings);

    /**
     * insert settings.
     *
     * @return the Settings
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Settings settings);

    /**
     * Check if the table has rows
     * @return the Settings
     */
    @Query("SELECT EXISTS(SELECT * FROM settings)")
    boolean isExists();
}
