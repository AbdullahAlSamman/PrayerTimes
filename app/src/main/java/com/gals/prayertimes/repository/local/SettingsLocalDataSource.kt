package com.gals.prayertimes.repository.local

import com.gals.prayertimes.repository.local.entities.SettingsEntity
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val settingsDao: SettingsDao
) {
    suspend fun upsertSettings(settingsEntity: SettingsEntity) =
        settingsDao.upsert(settingsEntity)

    suspend fun isSettingsExists(): Boolean =
        settingsDao.isExists()

    suspend fun getSettings(): SettingsEntity =
        settingsDao.settingsEntity()
}