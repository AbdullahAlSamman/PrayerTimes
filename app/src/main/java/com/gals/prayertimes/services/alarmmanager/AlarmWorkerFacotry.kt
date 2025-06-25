package com.gals.prayertimes.services.alarmmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.gals.prayertimes.repository.Repository
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val repository: Repository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = AlarmWorker(
        repo = repository,
        appContext = appContext,
        workerParams = workerParameters
    )
}