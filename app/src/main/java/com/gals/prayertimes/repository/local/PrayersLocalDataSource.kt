package com.gals.prayertimes.repository.local

import com.gals.prayertimes.repository.local.entities.PrayerEntity
import javax.inject.Inject

class PrayersLocalDataSource @Inject constructor(
    private val prayerDao: PrayerDao
) {
    suspend fun insertPrayers(prayer: PrayerEntity) =
        prayerDao.insert(prayer)

    suspend fun isTodayPrayerExists(todayDate: String): Boolean =
        prayerDao.isExists(todayDate)

    suspend fun getPrayers(todayDate: String): PrayerEntity =
        prayerDao.findByDate(todayDate)
}