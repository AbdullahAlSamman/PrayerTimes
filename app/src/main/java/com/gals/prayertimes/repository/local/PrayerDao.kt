package com.gals.prayertimes.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gals.prayertimes.repository.local.entities.PrayerEntity

/**
 * The interface Prayer dao.
 * defines all the queries as function with sql query to retrieve or insert or delete data from data base the implementation with be generated in compile time form Room Library.
 */
@Dao
interface PrayerDao {
    /**
     * Find times of a certain date.
     *
     * @param todayDate as string with "dd.MM.YYYY" formatted
     * @return a prayer entity
     */
    @Query("SELECT * FROM prayers WHERE sDate LIKE :todayDate  LIMIT 1")
    suspend fun findByDate(todayDate: String): PrayerEntity

    /**
     * Check if the prayers for today already saved into db
     *
     * @param todayDate as string with "dd.MM.YYYY" formatted
     * @return a Boolean value
     * */
    @Query("SELECT EXISTS(SELECT * FROM prayers WHERE sDate LIKE :todayDate  LIMIT 1) ")
    suspend fun isExists(todayDate: String): Boolean

    /**
     * Insert prayers into database
     *
     * @param prayer as prayer entity object
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayer: PrayerEntity)
}