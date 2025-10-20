package com.gals.prayertimes.di

import android.app.AlarmManager
import android.content.Context
import android.os.PowerManager
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HandlersModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat = NotificationManagerCompat.from(context)

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager = context.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun providePowerManager(
        @ApplicationContext context: Context
    ): PowerManager = context.getSystemService(PowerManager::class.java) as PowerManager
}