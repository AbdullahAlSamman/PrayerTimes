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
        val prayer: EntityPrayer? = getPrayerFromLocalDataSource(todayDate)
        if (prayer != null) {
            if (prayer.isValid()) {
                Log.e("Data Request", "exists locally in cache")
                return true
            }
        }
        val result = getPrayerFromRemoteDataSource(todayDate)
        if (result?.isSuccessful == true) {
            savePrayerToLocalDataSource(result.body()!!.first().toEntity())
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
            saveSettingsToLocalDataSource(settings)
            false
        }

    fun getPrayerFromLocalDataSource(todayDate: String): EntityPrayer? =
        database.prayerDao.findByDate(todayDate)

    fun getSettingsFromLocalDataSource(): Settings? = database.settingsDao.settings

    fun saveSettingsToLocalDataSource(settings: Settings) = database.settingsDao.insert(settings)

    private suspend fun getPrayerFromRemoteDataSource(todayDate: String) =
        prayerService?.getTodayPrayer(todayDate)

    private fun isSettingsExists(): Boolean = database.settingsDao.isExists()

    private fun savePrayerToLocalDataSource(prayer: EntityPrayer) =
        database.prayerDao.insert(prayer)
}