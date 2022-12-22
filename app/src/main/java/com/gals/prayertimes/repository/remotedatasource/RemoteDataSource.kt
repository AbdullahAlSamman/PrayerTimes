package com.gals.prayertimes.repository.remotedatasource

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val prayerService: PrayerService
) {

    suspend fun getPrayer(todayDate: String) =
        prayerService.getTodayPrayer(todayDate)
}