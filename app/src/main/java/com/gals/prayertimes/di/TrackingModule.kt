package com.gals.prayertimes.di

import com.gals.prayertimes.main.tracker.MainTracker
import com.gals.prayertimes.main.tracker.MainTrackerImpl
import com.gals.prayertimes.permissions.tracker.PermissionTracker
import com.gals.prayertimes.permissions.tracker.PermissionTrackerImpl
import com.gals.prayertimes.settings.notification.tracker.NotificationSettingsTracker
import com.gals.prayertimes.settings.notification.tracker.NotificationSettingsTrackerImpl
import com.gals.prayertimes.tracking.FirebaseTracker
import com.gals.prayertimes.tracking.Tracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackingModule {

    @Binds
    @Singleton
    abstract fun bindTracker(tracker: FirebaseTracker): Tracker


    @Binds
    @Singleton
    abstract fun bindMainTracker(mainTracker: MainTrackerImpl): MainTracker

    @Binds
    @Singleton
    abstract fun bindPermissionTracker(permissionTracker: PermissionTrackerImpl): PermissionTracker

    @Binds
    @Singleton
    abstract fun bindNotificationSettingsTracker(notificationSettingsTracker: NotificationSettingsTrackerImpl): NotificationSettingsTracker
}
