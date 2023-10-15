package com.gals.prayertimes.repository.local

import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val prayerDao: PrayerDao,
    private val settingsDao: SettingsDao
) {
    suspend fun insertSettings(settingsEntity: SettingsEntity) =
        settingsDao.insert(settingsEntity)

    suspend fun insertPrayers(prayer: PrayerEntity) =
        prayerDao.insert(prayer)

    suspend fun isSettingsExists(): Boolean =
        settingsDao.isExists()

    suspend fun isTodayPrayerExists(todayDate: String): Boolean =
        prayerDao.isExists(todayDate)

    suspend fun getPrayers(todayDate: String): PrayerEntity =
        prayerDao.findByDate(todayDate)

    suspend fun getSettings(): SettingsEntity =
        settingsDao.settingsEntity()
}