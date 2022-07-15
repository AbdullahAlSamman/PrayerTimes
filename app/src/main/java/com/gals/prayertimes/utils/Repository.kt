package com.gals.prayertimes.utils

import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.entities.Prayer.Companion.isValid
import com.gals.prayertimes.db.entities.Settings
import com.gals.prayertimes.network.PrayerService

class Repository(
    private val database: AppDB,
    private val prayerService: PrayerService?
) {

    suspend fun refreshPrayer(todayDate: String): Boolean {
        val prayer: EntityPrayer? = refreshPrayerFromDB(todayDate)
        if (prayer?.isValid() == true) {
            Log.e("Data Request", "exists locally in cache")
            return true
        }
        val result = getPrayerFromNetwork(todayDate)
        if (result?.isSuccessful == true) {
            savePrayer(result.body()!!.first().toEntity())
            return true
        } else {
            Log.e("Data Request", result?.message().toString())
        }
        return false
    }

    suspend fun refreshSettings(settings: Settings): Boolean {
        if (isSettingsExists()) {
            saveSettings(settings)
            return true
        } else {
            saveSettings(
                Settings(
                    false,
                    "silent"
                )
            )
            return false
        }
    }

    fun refreshPrayerFromDB(todayDate: String) = database.prayerDao.findByDate(todayDate)

    suspend fun getSettings(): Settings? = database.settingsDao.settings

    private suspend fun getPrayerFromNetwork(todayDate: String) =
        prayerService?.getTodayPrayer(todayDate)

    private fun saveSettings(settings: Settings) = database.settingsDao.insert(settings)

    private fun isSettingsExists(): Boolean = database.settingsDao.isExists

    private fun savePrayer(prayer: EntityPrayer) = database.prayerDao.insert(prayer)

}