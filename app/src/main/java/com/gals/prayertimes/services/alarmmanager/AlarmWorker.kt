package com.gals.prayertimes.services.alarmmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gals.prayertimes.model.mappers.todayDate
import com.gals.prayertimes.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val repo: Repository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val prayer = repo.getPrayer(todayDate())
        val settings = repo.getSettings()
        //TODO: do alarm scheduling logic
        scheduleNextAlarmWorker()

        return Result.success()
    }

    private fun scheduleNextAlarmWorker() {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val midnightNextDay =
            LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()).plusDays(1), LocalTime.MIDNIGHT)

        val delay = Duration.between(now, midnightNextDay).toMillis()

        val nextAlarmWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            PRAYER_ALARM_WORK_NEXT_DAY_NAME,
            ExistingWorkPolicy.APPEND,
            nextAlarmWorkRequest
        )
    }
}

internal const val PRAYER_ALARM_WORK_NAME = "PrayerAlarmPeriodicWork"
internal const val PRAYER_ALARM_WORK_NEXT_DAY_NAME = "PrayerAlarmPeriodicWork"