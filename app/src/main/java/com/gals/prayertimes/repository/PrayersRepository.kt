package com.gals.prayertimes.repository

import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.IODispatcher
import com.gals.prayertimes.common.NetworkException
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.common.mappers.toEntity
import com.gals.prayertimes.repository.local.PrayersLocalDataSource
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.repository.remote.PrayersRemoteDataSource
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.utils.SystemUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class PrayersRepository @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val prayersLocalDataSource: PrayersLocalDataSource,
    private val prayersRemoteDataSource: PrayersRemoteDataSource,
    private val utils: SystemUtils
) {
    fun fetchPrayer(todayDate: String): Flow<PrayerEntity> = flow {
        if (prayersLocalDataSource.isTodayPrayerExists(todayDate)) {
            Timber.i("exists locally in cache")
            emit(prayersLocalDataSource.getPrayers(todayDate))
            return@flow
        }
        if (utils.isNetworkAvailable()) {
            val result = prayersRemoteDataSource.getPrayers(todayDate)
            if (result.isSuccessful) {
                result.body()?.let { response ->
                    checkServerError(response)
                    Timber.i("Success: ${result.message()}")
                    prayersLocalDataSource.insertPrayers(response.toEntity())
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

    suspend fun getLocalPrayer(todayDate: String): PrayerEntity = prayersLocalDataSource.getPrayers(todayDate)

    private fun checkServerError(response: PrayersResponse) {
        if (response == PrayersResponse("", "", emptyList())) {
            throw ServerException("Server error: No data")
        }
    }
}