package com.gals.prayertimes.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gals.prayertimes.EntityPrayer

/**
 * The interface Prayer dao.
 * defines all the queries as function with sql query to retrive or insert or delete data from data base the implementation with be generated in compile time form Room Library.
 */
@Dao
interface PrayerDao {
    /**
     * Find times of a certain date.
     *
     * @param arg0 the today date
     * @return the prayer
     */
    @Query("SELECT * FROM prayers WHERE sDate LIKE :arg0  LIMIT 1")
    fun findByDate(arg0: String?): EntityPrayer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(prayer: EntityPrayer?)
}