package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.BR
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.entities.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AthanSettingsViewModel(
    private val repository: Repository
) : ViewModel() {
    val alarm: ObservableBoolean = ObservableBoolean(false)
    val radioGroupObserver = RadioGroupObserver()

    fun alarmToggle(checked: Boolean) {
        alarm.set(checked)
        Log.i("changed the athan toggle", checked.toString())
    }

    fun getSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                radioGroupObserver.setNotificationInfo(repository.getSettings())
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

    inner class RadioGroupObserver : BaseObservable() {
        var notificationType: ObservableField<String> = ObservableField()

        @get:Bindable
        var fullAthan: Boolean = false
            set(value) {
                field = value
                if (value) {
                    notificationType.set(NotificationType.FULL.value)
                }
                notifyPropertyChanged(BR.fullAthan)
            }

        @get:Bindable
        var halfAthan: Boolean = false
            set(value) {
                field = value
                if (value) {
                    notificationType.set(NotificationType.HALF.value)
                }
                notifyPropertyChanged(BR.halfAthan)
            }

        @get:Bindable
        var toneAthan: Boolean = false
            set(value) {
                field = value
                if (value) {
                    notificationType.set(NotificationType.TONE.value)
                }
                notifyPropertyChanged(BR.toneAthan)
            }

        @get:Bindable
        var silentAthan: Boolean = false
            set(value) {
                field = value
                if (value) {
                    notificationType.set(NotificationType.SILENT.value)
                }
                notifyPropertyChanged(BR.silentAthan)
            }


        fun setNotificationInfo(settings: Settings?) {
            alarm.set(settings?.notification == true)
            notificationType.set(settings?.notificationType)
            when (settings?.notificationType) {
                NotificationType.FULL.value -> fullAthan = true
                NotificationType.HALF.value -> halfAthan = true
                NotificationType.TONE.value -> toneAthan = true
                NotificationType.SILENT.value -> silentAthan = true
            }
        }
    }
}
