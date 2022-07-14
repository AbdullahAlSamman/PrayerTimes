package com.gals.prayertimes.utils

import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.network.PrayerService

class Repository constructor(
    private val database: AppDB,
    private val prayerService: PrayerService?
) {

    suspend fun getPrayer(todayDate: String) = prayerService?.getTodayPrayer(todayDate)

}