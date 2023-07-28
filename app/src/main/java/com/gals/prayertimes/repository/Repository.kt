package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.NetworkException
import com.gals.prayertimes.repository.local.LocalDataSource
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.repository.remote.RemoteDataSource
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val tools: UtilsManager
) {

    suspend fun fetchComposePrayer(todayDate: String): Flow<PrayerEntity> = flow {
        if (localDataSource.isTodayPrayerExists(todayDate)) {
            Log.e("Local Data Request", "exists locally in cache")
            emit(localDataSource.getPrayers(todayDate))
            return@flow
        }
        if (tools.isNetworkAvailable()) {
            val result = remoteDataSource.getPrayers(todayDate)
            if (result.isSuccessful) {
                result.body()?.let { response ->
                    Log.e("Remote Data Request", "Success: ${result.message()}")
                    localDataSource.insertPrayers(response.toEntity())
                    emit(response.toEntity())
                }
            } else {
                Log.e("Remote Data Request", "Network error: ${result.message()}")
                throw NetworkException("${result.code()}: ${result.message()}")
            }
        } else {
            Log.e("Remote Data Request", "Connectivity: No Internet")
            throw ConnectivityException("No Internet")
        }
    }

    suspend fun getPrayer(todayDate: String): PrayerEntity =
        localDataSource.getPrayers(todayDate)

    suspend fun getSettings(): SettingsEntity = localDataSource.getSettings()

    suspend fun saveSettings(settingsEntity: SettingsEntity) =
        localDataSource.insertSettings(settingsEntity)

}