package com.gals.prayertimes.settings

import androidx.lifecycle.ViewModel
import com.gals.prayertimes.permissions.manager.PermissionsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsMenuViewModel @Inject constructor(
    private val permissionsManager: PermissionsManager
) : ViewModel() {

    fun areAllPermissionsGranted(): Boolean = permissionsManager.areAllPermissionsGranted()
}