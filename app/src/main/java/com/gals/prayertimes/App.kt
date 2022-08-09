package com.gals.prayertimes

import android.app.Application
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.utils.UtilsManager

typealias DomainPrayer = Prayer
typealias EntityPrayer = com.gals.prayertimes.repository.db.entities.Prayer

class App : Application() {
    lateinit var tools: UtilsManager
    lateinit var prayer: Prayer
    lateinit var db: AppDB
}