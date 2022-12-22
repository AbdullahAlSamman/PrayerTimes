package com.gals.prayertimes.di

import android.content.Context
import androidx.room.Room
import com.gals.prayertimes.repository.local.AppDB
import com.gals.prayertimes.repository.local.PrayerDao
import com.gals.prayertimes.repository.local.SettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providePrayerDao(appDB: AppDB): PrayerDao =
        appDB.prayerDao

    @Provides
    fun provideSettingsDao(appDB: AppDB): SettingsDao =
        appDB.settingsDao

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDB =
        Room.databaseBuilder(
            context.applicationContext,
            AppDB::class.java,
            AppDB.DB_NAME
        ).build()
}