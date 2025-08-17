package com.gals.prayertimes

import android.app.Application
import androidx.work.Configuration
import com.gals.prayertimes.logging.AppDebugTree
import com.gals.prayertimes.services.alarmmanager.AlarmWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: AlarmWorkerFactory

    @Inject
    lateinit var appDebugTree: AppDebugTree

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(appDebugTree)
        }
    }
}
