package com.gals.prayertimes.viewmodel

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.Settings
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.observer.RadioGroupObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AthanSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository,
    private val tools: UtilsManager
) : ViewModel() {
    private lateinit var musicPlayer: MediaPlayer
    private lateinit var currentNotificationType: String
    private val mediaJob = Job()
    private val mediaScope = CoroutineScope(Dispatchers.Main + mediaJob)
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
                setNotificationInfo(repository.getSettings())
            }
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveSettings(
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

    fun startNotificationService(serviceClass: Class<*>) {
        if (alarm.get()) {
            if (currentNotificationType == radioGroupObserver.notificationType.get()) {
                return
            } else {
                tools.restartService(serviceClass)
            }
        } else {
            if (tools.isServiceRunning(serviceClass)) {
                tools.stopService(serviceClass)
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

    private fun setNotificationInfo(settings: Settings?) {
        alarm.set(settings?.notification == true)
        radioGroupObserver.notificationType.set(settings?.notificationType)
        currentNotificationType = settings?.notificationType.toString()
        when (settings?.notificationType) {
            NotificationType.FULL.value -> radioGroupObserver.fullAthan = true
            NotificationType.HALF.value -> radioGroupObserver.halfAthan = true
            NotificationType.TONE.value -> radioGroupObserver.toneAthan = true
            NotificationType.SILENT.value -> radioGroupObserver.silentAthan = true
        }
    }
}
