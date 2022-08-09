package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.entities.Prayer.Companion.isValid
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.repository.network.PrayerService
import com.gals.prayertimes.utils.toEntity

class Repository(
    private val database: AppDB
) {
    private val prayerService: PrayerService? = PrayerService.getInstance()

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

    fun refreshSettings(
        settings: Settings = Settings(
            notification = false,
            notificationType = NotificationType.SILENT.value
        )
    ): Boolean =
        if (isSettingsExists()) {
            true
        } else {
            saveSettings(settings)
            false
        }

    fun refreshPrayerFromDB(todayDate: String) = database.prayerDao.findByDate(todayDate)

    fun getSettings(): Settings? = database.settingsDao.settings

    fun saveSettings(settings: Settings) = database.settingsDao.insert(settings)

    private suspend fun getPrayerFromNetwork(todayDate: String) =
        prayerService?.getTodayPrayer(todayDate)

    private fun isSettingsExists(): Boolean = database.settingsDao.isExists()

    private fun savePrayer(prayer: EntityPrayer) = database.prayerDao.insert(prayer)
}