package com.gals.prayertimes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _uiSelectedRadio = MutableStateFlow(NotificationType.SILENT.value)
    private val _uiSwitchState = MutableStateFlow(false)

    val uiSelectedRadio: StateFlow<String> = _uiSelectedRadio.asStateFlow()
    val uiSwitchState: StateFlow<Boolean> = _uiSwitchState.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = repository.getSettings()
            _uiSelectedRadio.update { settings?.notificationType.toString() }
            _uiSwitchState.update { settings?.notification == true }
        }
    }

    fun updateSelectedRadio(value: String) {
        _uiSelectedRadio.update { value }
        updateSettings(
            SettingsEntity(notificationType = value, notification = _uiSwitchState.value)
        )
    }

    fun updateSwitchState(value: Boolean) {
        _uiSwitchState.update { value }
        updateSettings(
            SettingsEntity(notificationType = _uiSelectedRadio.value, notification = value)
        )
    }

    private fun updateSettings(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(settingsEntity)
        }
    }
}