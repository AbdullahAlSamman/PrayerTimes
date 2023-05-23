package com.gals.prayertimes.repository.local

import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val prayerDao: PrayerDao,
    private val settingsDao: SettingsDao
) {
    fun insertSettings(settingsEntity: SettingsEntity) =
        settingsDao.insert(settingsEntity)

    fun insertPrayers(prayer: PrayerEntity) =
        prayerDao.insert(prayer)

    fun isSettingsExists(): Boolean =
        settingsDao.isExists()

    fun isTodayPrayerExists(todayDate: String): Boolean =
        prayerDao.isExists(todayDate)

    fun getPrayer(todayDate: String): PrayerEntity? =
        prayerDao.findByDate(todayDate)

    fun getSettings(): SettingsEntity? =
        settingsDao.settingsEntity
}