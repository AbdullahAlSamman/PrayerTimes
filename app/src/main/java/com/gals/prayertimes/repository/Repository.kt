package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.localdatasource.AppDB
import com.gals.prayertimes.repository.localdatasource.LocalDataSource
import com.gals.prayertimes.repository.localdatasource.entities.Prayer.Companion.isValid
import com.gals.prayertimes.repository.localdatasource.entities.Settings
import com.gals.prayertimes.repository.remotedatasource.PrayerService
import com.gals.prayertimes.repository.remotedatasource.RemoteDataSource
import com.gals.prayertimes.utils.toEntity
import javax.inject.Inject

class Repository @Inject constructor(
   private val localDataSource: LocalDataSource,
   private val remoteDataSource: RemoteDataSource
) {

    suspend fun refreshPrayer(todayDate: String): Boolean {
        val prayer: EntityPrayer? = localDataSource.getPrayer(todayDate)
        if (prayer != null) {
            if (prayer.isValid()) {
                Log.e("Data Request", "exists locally in cache")
                return true
            }
        }
        val result = remoteDataSource.getPrayer(todayDate)
        if (result.isSuccessful) {
            localDataSource.insertPrayers(result.body()!!.first().toEntity())
            return true
        } else {
            Log.e("Data Request", result.message().toString())
        }
        return false
    }

    fun refreshSettings(
        settings: Settings = Settings(
            notification = false,
            notificationType = NotificationType.SILENT.value
        )
    ): Boolean =
        if (localDataSource.isSettingsExists()) {
            true
        } else {
            localDataSource.insertSettings(settings)
            false
        }

    fun getPrayerFromLocalDataSource(todayDate: String): EntityPrayer? =
        localDataSource.getPrayer(todayDate)

    fun getSettings(): Settings? = localDataSource.getSettings()

    fun saveSettings(settings: Settings) = localDataSource.insertSettings(settings)

}