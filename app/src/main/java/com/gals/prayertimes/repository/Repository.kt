package com.gals.prayertimes.repository

import android.util.Log
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.IODispatcher
import com.gals.prayertimes.model.NetworkException
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.ServerException
import com.gals.prayertimes.model.mappers.toEntity
import com.gals.prayertimes.repository.local.LocalDataSource
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import com.gals.prayertimes.repository.remote.RemoteDataSource
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.utils.SystemUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val utils: SystemUtils,
) {
    fun fetchPrayer(todayDate: String): Flow<PrayerEntity> = flow {
        if (localDataSource.isTodayPrayerExists(todayDate)) {
            Log.i("ngz_local_data_request", "exists locally in cache")
            emit(localDataSource.getPrayers(todayDate))
            return@flow
        }
        if (utils.isNetworkAvailable()) {
            val result = remoteDataSource.getPrayers(todayDate)
            if (result.isSuccessful) {
                result.body()?.let { response ->
                    checkServerError(response)
                    Log.i(LOG_TAG, "Success: ${result.message()}")
                    localDataSource.insertPrayers(response.toEntity())
                    emit(response.toEntity())
                }
            } else {
                Log.e(LOG_TAG, "Network error: ${result.message()}")
                throw NetworkException("${result.code()}: ${result.message()}")
            }
        } else {
            Log.e(LOG_TAG, "Connectivity error: No Internet")
            throw ConnectivityException("No Internet")
        }
    }.flowOn(dispatcher)

    suspend fun getPrayer(todayDate: String): PrayerEntity = localDataSource.getPrayers(todayDate)

    suspend fun getSettings(): SettingsEntity =
        if (localDataSource.isSettingsExists()) {
            localDataSource.getSettings()
        } else {
            val settings = SettingsEntity(
                notificationType = NotificationType.SILENT,
                notification = false
            )
            localDataSource.insertSettings(settings)
            settings
        }

    suspend fun saveSettings(settingsEntity: SettingsEntity) =
        localDataSource.insertSettings(settingsEntity)
    
    private fun checkServerError(response: PrayersResponse) {
        if (response == PrayersResponse("", "", emptyList())) {
            throw ServerException("Server error: No data")
        }
    }
}

private const val LOG_TAG = "ngz_remote_data_request"