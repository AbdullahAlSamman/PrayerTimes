package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.local.LocalDataSource
import com.gals.prayertimes.repository.local.entities.Prayer.Companion.isValid
import com.gals.prayertimes.repository.local.entities.Settings
import com.gals.prayertimes.repository.remote.RemoteDataSource
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

    fun getPrayer(todayDate: String): EntityPrayer? =
        localDataSource.getPrayer(todayDate)

    fun getSettings(): Settings? = localDataSource.getSettings()

    fun saveSettings(settings: Settings) = localDataSource.insertSettings(settings)

}