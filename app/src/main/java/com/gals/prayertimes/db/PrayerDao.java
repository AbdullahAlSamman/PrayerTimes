package com.gals.prayertimes.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.gals.prayertimes.model.Prayer;

/**
 * The interface Prayer dao.
 * defines all the queries as function with sql query to retrive or insert or delete data from data base the implementation with be generated in compile time form Room Library.
 */
@Dao
public interface PrayerDao {

    /**
     * Find times of a certain date.
     *
     * @param date the today date
     * @return the prayer
     */
    @Query("SELECT * FROM prayers WHERE sDate LIKE :date  LIMIT 1")
    Prayer findByDate(String date);

    @Insert
    void insert(Prayer prayer);


}
