package com.gals.prayertimes.viewmodel

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.services.NotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.observer.RadioGroupObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AthanSettingsViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {
    private lateinit var musicPlayer: MediaPlayer
    private val mediaJob = Job()
    private val mediaScope = CoroutineScope(Dispatchers.Main + mediaJob)
    private val tools = UtilsManager(context)
    val radioGroupObserver = RadioGroupObserver()
    val alarm: ObservableBoolean = ObservableBoolean(false)
    val isPlaying: ObservableBoolean = ObservableBoolean(false)
    val athanFullVisibility: ObservableBoolean = ObservableBoolean(true)
    val athanHalfVisibility: ObservableBoolean = ObservableBoolean(true)

    fun alarmToggle(checked: Boolean) {
        alarm.set(checked)
        Log.i("changed the athan toggle", checked.toString())
    }

    fun playFullAthan() {
        if (!this::musicPlayer.isInitialized) {
            initMediaPlayer()
        }
        mediaScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                if (musicPlayer.isPlaying) {
                    stopMediaPlayer()
                } else {
                    athanHalfVisibility.set(false)
                    setMediaDataSource(NotificationType.FULL)
                    startMediaPlayer()
                }
            }
        }
    }

    fun playHalfAthan() {
        if (!this::musicPlayer.isInitialized) {
            initMediaPlayer()
        }
        mediaScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                if (musicPlayer.isPlaying) {
                    stopMediaPlayer()
                } else {
                    athanFullVisibility.set(false)
                    setMediaDataSource(NotificationType.HALF)
                    startMediaPlayer()
                }
            }
        }
    }

    fun getSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setNotificationInfo(repository.getSettingsFromLocalDataSource())
            }
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveSettingsToLocalDataSource(
                    Settings(
                        notification = alarm.get(),
                        notificationType = radioGroupObserver.notificationType.get()!!
                    )
                )
            }
        }
    }

    fun stopMediaPlayer() {
        if (this::musicPlayer.isInitialized) {
            isPlaying.set(false)
            musicPlayer.pause()
            musicPlayer.seekTo(0)
            athanFullVisibility.set(true)
            athanHalfVisibility.set(true)
        }
    }

    fun startNotificationService() {
        if (alarm.get()) {
            restartService()
        } else {
            if (tools.isServiceRunning(NotificationService::class.java)) {
                context.stopService(Intent(context, NotificationService::class.java))
            }
        }
    }

    private fun initMediaPlayer() {
        musicPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build()
            )
        }
        musicPlayer.setOnCompletionListener {
            stopMediaPlayer()
        }
    }

    private fun setMediaDataSource(notificationType: NotificationType) {
        musicPlayer.reset()
        musicPlayer.setDataSource(
            context,
            tools.getSoundUri(notificationType)
        )
        musicPlayer.prepare()
    }

    private fun startMediaPlayer() {
        musicPlayer.start()
        isPlaying.set(true)
    }

    private fun restartService() {
        context.stopService(Intent(context, NotificationService::class.java))
        context.startService(Intent(context, NotificationService::class.java))
    }

    private fun setNotificationInfo(settings: Settings?) {
        alarm.set(settings?.notification == true)
        radioGroupObserver.notificationType.set(settings?.notificationType)
        when (settings?.notificationType) {
            NotificationType.FULL.value -> radioGroupObserver.fullAthan = true
            NotificationType.HALF.value -> radioGroupObserver.halfAthan = true
            NotificationType.TONE.value -> radioGroupObserver.toneAthan = true
            NotificationType.SILENT.value -> radioGroupObserver.silentAthan = true
        }
    }
}
