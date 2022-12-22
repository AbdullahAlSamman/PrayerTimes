package com.gals.prayertimes

import android.app.Application
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.repository.localdatasource.AppDB
import com.gals.prayertimes.utils.UtilsManager
import dagger.hilt.android.HiltAndroidApp

typealias DomainPrayer = Prayer
typealias EntityPrayer = com.gals.prayertimes.repository.localdatasource.entities.Prayer

@HiltAndroidApp
class App : Application() {
    lateinit var tools: UtilsManager
    lateinit var prayer: Prayer
    lateinit var db: AppDB
}