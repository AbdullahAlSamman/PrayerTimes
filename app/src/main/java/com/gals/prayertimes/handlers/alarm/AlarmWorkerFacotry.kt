package com.gals.prayertimes.handlers.alarm

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.repository.SettingsRepository
import com.gals.prayertimes.utils.PrayerCalculation
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val prayersRepository: PrayersRepository,
    private val settingsRepository: SettingsRepository,
    private val alarmHandler: AlarmHandler,
    private val prayerCalculation: PrayerCalculation
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = AlarmWorker(
        prayersRepository = prayersRepository,
        settingsRepository = settingsRepository,
        alarmHandler = alarmHandler,
        prayerCalculation = prayerCalculation,
        appContext = appContext,
        workerParams = workerParameters
    )
}
