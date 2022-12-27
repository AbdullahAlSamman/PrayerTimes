package com.gals.prayertimes.repository.local

import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.repository.local.entities.Settings
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val prayerDao: PrayerDao,
    private val settingsDao: SettingsDao
) {
    fun insertSettings(settings: Settings) =
        settingsDao.insert(settings)

    fun insertPrayers(prayer: EntityPrayer) =
        prayerDao.insert(prayer)

    fun isSettingsExists(): Boolean =
        settingsDao.isExists()

    fun isTodayPrayerExists(todayDate: String): Boolean =
        prayerDao.isExists(todayDate)

    fun getPrayer(todayDate: String): EntityPrayer? =
        prayerDao.findByDate(todayDate)

    fun getSettings(): Settings? =
        settingsDao.settings
}