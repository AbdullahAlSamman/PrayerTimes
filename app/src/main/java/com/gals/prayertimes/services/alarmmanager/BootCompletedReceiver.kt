package com.gals.prayertimes.services.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gals.prayertimes.model.IODispatcher
import com.gals.prayertimes.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: Repository

    @Inject
    @IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    /**
     * This receiver is designed to reschedule lost alarms after reboot plus
     * making sure [AlarmWorker] despite being persistent is rescheduled.
     * */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("ngz_alarms", "checking alarms after reboot")
            context?.let {
                CoroutineScope(ioDispatcher).launch {
                    if (repository.getSettings().notification) {
                        val prayerAlarmWorkRequest =
                            OneTimeWorkRequestBuilder<AlarmWorker>().build()
                        WorkManager.getInstance(context).enqueueUniqueWork(
                            PRAYER_ALARM_WORK_NAME,
                            ExistingWorkPolicy.REPLACE,
                            prayerAlarmWorkRequest
                        )
                        Log.i("ngz_alarms", "works and alarms scheduled after reboot")
                    }
                }
            }
        }
    }
}