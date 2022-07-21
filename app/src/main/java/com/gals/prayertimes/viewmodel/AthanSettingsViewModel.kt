package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.gals.prayertimes.repository.Repository

class AthanSettingsViewModel(
    repository: Repository
) : ViewModel() {
    val alarm: ObservableBoolean = ObservableBoolean(false)
    val fullAthan: ObservableBoolean = ObservableBoolean(false)
    val halfAthan: ObservableBoolean = ObservableBoolean(false)
    val toneAthan: ObservableBoolean = ObservableBoolean(false)
    val silentAthan: ObservableBoolean = ObservableBoolean(false)

    fun alarmToggle(checked: Boolean) {
        alarm.set(checked)
        Log.i("changed the athan toggle", checked.toString())
    }

}