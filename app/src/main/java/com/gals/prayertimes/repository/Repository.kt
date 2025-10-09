package com.gals.prayertimes.repository

import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.IODispatcher
import com.gals.prayertimes.common.NetworkException
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.common.mappers.toEntity
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
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val utils: SystemUtils,
) {
    fun fetchPrayer(todayDate: String): Flow<PrayerEntity> = flow {
        if (localDataSource.isTodayPrayerExists(todayDate)) {
            Timber.i("exists locally in cache")
            emit(localDataSource.getPrayers(todayDate))
            return@flow
        }
        if (utils.isNetworkAvailable()) {
            val result = remoteDataSource.getPrayers(todayDate)
            if (result.isSuccessful) {
                result.body()?.let { response ->
                    checkServerError(response)
                    Timber.i("Success: ${result.message()}")
                    localDataSource.insertPrayers(response.toEntity())
                    emit(response.toEntity())
                }
            } else {
                Timber.e("Network error: ${result.message()}")
                throw NetworkException("${result.code()}: ${result.message()}")
            }
        } else {
            Timber.e("Connectivity error: No Internet")
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