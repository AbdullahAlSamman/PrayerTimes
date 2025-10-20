package com.gals.prayertimes.handlers.alarm

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.PrayerCalculation
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val repository: Repository,
    private val alarmHandler: AlarmHandler,
    private val prayerCalculation: PrayerCalculation
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = AlarmWorker(
        repository = repository,
        alarmHandler = alarmHandler,
        prayerCalculation = prayerCalculation,
        appContext = appContext,
        workerParams = workerParameters
    )
}
