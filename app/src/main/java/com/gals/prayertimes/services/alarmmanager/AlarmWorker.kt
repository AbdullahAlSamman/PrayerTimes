package com.gals.prayertimes.services.alarmmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.qualifiers.ApplicationContext

class AlarmWorker(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {

        return Result.success()
    }
}