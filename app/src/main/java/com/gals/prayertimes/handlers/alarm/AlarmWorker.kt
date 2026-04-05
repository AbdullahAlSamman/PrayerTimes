package com.gals.prayertimes.handlers.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.TimePrayer
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.getTimePrayerByName
import com.gals.prayertimes.common.mappers.toTimePrayer
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.repository.SettingsRepository
import com.gals.prayertimes.repository.local.entities.SettingsEntity.Companion.toPrayerNotification
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.todayDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
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
    @Assisted private val prayersRepository: PrayersRepository,
    @Assisted private val settingsRepository: SettingsRepository,
    @Assisted private val alarmHandler: AlarmHandler,
    @Assisted private val prayerCalculation: PrayerCalculation
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        prayersRepository.getPrayer(todayDate())
            .collect { prayer ->
                val settings = settingsRepository.getSavedSettings()
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
        selectedPrayerNotifications: Map<UiPrayerName, Boolean>
    ) {
        selectedPrayerNotifications.forEach { (prayerName, enabled) ->
            val prayer = prayerCalculation.getNextPrayerLocalTime(
                timePrayers.getTimePrayerByName(prayerName)
            )
            if (prayer?.isAfter(LocalDateTime.now()) == true && enabled) {
                alarmHandler.scheduleAlarm(
                    PrayerAlarmItem(
                        time = prayer,
                        prayer = prayerName.name,
                        notificationType = notificationType.name
                    )
                )
                Timber.i("$prayerName alarm has been set with type ${notificationType.name}")
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
            Timber.i("Next day worker scheduled to run at: $targetExecutionTime")
        }
    }
}

internal const val PRAYER_ALARM_WORK_NAME = "PrayerAlarmPeriodicWork"
internal const val PRAYER_ALARM_WORK_NEXT_DAY_NAME = "PrayerAlarmNextDayWork"
