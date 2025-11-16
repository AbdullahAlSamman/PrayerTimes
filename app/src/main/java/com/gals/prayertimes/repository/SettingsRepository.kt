package com.gals.prayertimes.repository

import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.repository.local.SettingsLocalDataSource
import com.gals.prayertimes.repository.local.entities.SettingsEntity
import javax.inject.Inject


class SettingsRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) {
    suspend fun getSavedSettings(): SettingsEntity =
        if (settingsLocalDataSource.isSettingsExists()) {
            settingsLocalDataSource.getSettings()
        } else {
            val settings = SettingsEntity(
                notificationType = NotificationType.SILENT,
                notification = false
            )
            settingsLocalDataSource.upsertSettings(settings)
            settings
        }

    suspend fun saveSettings(settingsEntity: SettingsEntity) =
        settingsLocalDataSource.upsertSettings(settingsEntity)
}