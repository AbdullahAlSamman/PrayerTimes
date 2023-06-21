package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.local.LocalDataSource
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.repository.remote.RemoteDataSource
import com.gals.prayertimes.utils.toEntity
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun refreshPrayer(todayDate: String): Boolean {
        if (localDataSource.isTodayPrayerExists(todayDate)) {
            Log.e("Remote Data Request", "exists locally in cache")
            return true
        }
        val result = remoteDataSource.getPrayer(todayDate)
        if (result.isSuccessful) {
            localDataSource.insertPrayers(result.body()!!.first().toEntity())
            return true
        } else {
            Log.e("Remote Data Request", result.message().toString())
        }
        return false
    }

    suspend fun refreshSettings(
        settingsEntity: SettingsEntity = SettingsEntity(
            notification = false,
            notificationType = NotificationType.SILENT.value
        )
    ): Boolean =
        if (localDataSource.isSettingsExists()) {
            true
        } else {
            localDataSource.insertSettings(settingsEntity)
            false
        }

    suspend fun getPrayer(todayDate: String): PrayerEntity? =
        localDataSource.getPrayer(todayDate)

    suspend fun getSettings(): SettingsEntity? = localDataSource.getSettings()

    suspend fun saveSettings(settingsEntity: SettingsEntity) =
        localDataSource.insertSettings(settingsEntity)

}