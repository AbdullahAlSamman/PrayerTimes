package com.gals.prayertimes

import android.app.Application
import com.gals.prayertimes.model.Prayer
import dagger.hilt.android.HiltAndroidApp

typealias DomainPrayer = Prayer
typealias EntityPrayer = com.gals.prayertimes.repository.localdatasource.entities.Prayer

@HiltAndroidApp
class App : Application()