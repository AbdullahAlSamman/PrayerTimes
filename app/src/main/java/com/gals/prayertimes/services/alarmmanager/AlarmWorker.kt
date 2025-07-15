package com.gals.prayertimes.services.alarmmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.mappers.getTimePrayerByName
import com.gals.prayertimes.model.mappers.toTimePrayer
import com.gals.prayertimes.model.mappers.todayDate
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity.Companion.toPrayerNotification
import com.gals.prayertimes.utils.PrayerCalculation
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
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val repository: Repository,
    @Assisted private val alarmManager: AlarmManager,
    @Assisted private val prayerCalculation: PrayerCalculation
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        repository.fetchPrayer(todayDate())
            .collect { prayer ->
                val settings = repository.getSettings()
                scheduleUpcomingAlarms(
                    timePrayers = prayer.toTimePrayer(),
                    selectedPrayerNotifications = settings.toPrayerNotification(),
                    notificationType = settings.notificationType
                )

                scheduleNextAlarmWorker()
            }

        return Result.success()
    }

    private fun scheduleUpcomingAlarms(
        timePrayers: TimePrayer,
        notificationType: NotificationType,
        selectedPrayerNotifications: Map<PrayerName, Boolean>
    ) {
        selectedPrayerNotifications.forEach { prayerName, isEnabled ->
            val prayer = prayerCalculation.getNextPrayerLocalTime(
                timePrayers.getTimePrayerByName(prayerName)
            )
            if (prayer?.isAfter(LocalDateTime.now()) == true) {
                alarmManager.scheduleAlarm(
                    PrayerAlarmItem(
                        time = prayer,
                        prayer = prayerName.name,
                        notificationType = notificationType.name
                    )
                )
                Log.i(
                    "ngz_alarm_set",
                    "$prayerName alarm has been set with type ${notificationType.name}"
                )
            }
        }
    }

    private fun scheduleNextAlarmWorker() {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val targetExecutionTime =
            LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()).plusDays(1), LocalTime.of(0, 5))

        val delay = Duration.between(now, targetExecutionTime).toMillis()

        if (delay > 0) {
            val nextAlarmWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(appContext).enqueueUniqueWork(
                PRAYER_ALARM_WORK_NEXT_DAY_NAME,
                ExistingWorkPolicy.REPLACE,
                nextAlarmWorkRequest
            )
            Log.i("AlarmWorker", "Next day worker scheduled to run at: $targetExecutionTime")
        }
    }
}

internal const val PRAYER_ALARM_WORK_NAME = "PrayerAlarmPeriodicWork"
internal const val PRAYER_ALARM_WORK_NEXT_DAY_NAME = "PrayerAlarmNextDayWork"
