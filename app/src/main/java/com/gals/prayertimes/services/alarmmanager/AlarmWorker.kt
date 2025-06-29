package com.gals.prayertimes.services.alarmmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gals.prayertimes.model.PrayerName
import com.gals.prayertimes.model.TimePrayer
import com.gals.prayertimes.model.mappers.getTimePrayerByName
import com.gals.prayertimes.model.mappers.toTimePrayer
import com.gals.prayertimes.model.mappers.todayDate
import com.gals.prayertimes.repository.Repository
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
        val prayer = repository.fetchPrayer(todayDate())
        val notificationSettings = repository.getPrayerNotification()
        prayer.collect {
            val timePrayers = it.toTimePrayer()

            scheduleUpcomingAlarms(
                timePrayers = timePrayers,
                selectedPrayerNotifications = notificationSettings
            )

            scheduleNextAlarmWorker()
        }

        return Result.success()
    }

    private fun scheduleUpcomingAlarms(
        timePrayers: TimePrayer,
        selectedPrayerNotifications: Map<PrayerName, Boolean>
    ) {
        selectedPrayerNotifications.forEach { prayerName, isEnabled ->
            val prayer = prayerCalculation.getNextPrayerLocalTime(
                timePrayers.getTimePrayerByName(prayerName)
            )
            if (prayer?.isAfter(LocalDateTime.now()) == true) {
                alarmManager.scheduleAlarm( // TODO: 1. replace test strings with notification strings.
                    AlarmItem(
                        time = prayer,
                        title = "${prayerName.name} Prayer",
                        message = "Time for ${prayerName.name} prayer."
                    )
                )
            }
        }
    }

    private fun scheduleNextAlarmWorker() {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val midnightNextDay =
            LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()).plusDays(1), LocalTime.MIDNIGHT)

        val delay = Duration.between(now, midnightNextDay).toMillis()

        val nextAlarmWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            PRAYER_ALARM_WORK_NEXT_DAY_NAME,
            ExistingWorkPolicy.APPEND,
            nextAlarmWorkRequest
        )
    }
}

internal const val PRAYER_ALARM_WORK_NAME = "PrayerAlarmPeriodicWork"
internal const val PRAYER_ALARM_WORK_NEXT_DAY_NAME = "PrayerAlarmPeriodicWork"