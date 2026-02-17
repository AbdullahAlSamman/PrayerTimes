package com.gals.prayertimes.permissions

import androidx.lifecycle.ViewModel
import com.gals.prayertimes.permissions.manager.PermissionsManager
import com.gals.prayertimes.permissions.model.PermissionType
import com.gals.prayertimes.permissions.tracker.PermissionTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionsManager: PermissionsManager,
    private val tracker: PermissionTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow<PermissionScreen.State>(PermissionScreen.State.Loading)
    val uiState: StateFlow<PermissionScreen.State> = _uiState.asStateFlow()

    init {
        tracker.permissionOpen()
        checkPermissions()
    }

    //region Permission intents
    fun requestPermission(permission: PermissionType) {
        permissionsManager.requestPermission(permission)
    }

    fun openSettings(permission: PermissionType) {
        permissionsManager.openSettings(permission)
    }

    fun updatePermissions() {
        _uiState.update { PermissionScreen.State.Loading }
        checkPermissions()
    }
    //endregion

    //region Lifecycle intents
    fun onClose(){
        tracker.permissionClose()
    }

    fun onFinish(){
        tracker.permissionsGranted(permissionsManager.areAllPermissionsGranted())
    }
    //endregion
    private fun checkPermissions() {
        _uiState.update {
            PermissionScreen.State.Content(
                permissions = PermissionType.entries.associate { permissionsManager.checkPermission(it) }
            )
        }
    }
}
