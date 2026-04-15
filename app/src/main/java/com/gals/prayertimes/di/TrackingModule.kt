package com.gals.prayertimes.di

import com.gals.prayertimes.main.tracker.MainTracker
import com.gals.prayertimes.main.tracker.MainTrackerImpl
import com.gals.prayertimes.permissions.tracker.PermissionTracker
import com.gals.prayertimes.permissions.tracker.PermissionTrackerImpl
import com.gals.prayertimes.settings.calendar.tracker.PrayerCalendarTracker
import com.gals.prayertimes.settings.calendar.tracker.PrayerCalendarTrackerImpl
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
interface TrackingModule {

    @Binds
    @Singleton
    fun bindTracker(tracker: FirebaseTracker): Tracker

    @Binds
    @Singleton
    fun bindMainTracker(impl: MainTrackerImpl): MainTracker

    @Binds
    @Singleton
    fun bindPermissionTracker(impl: PermissionTrackerImpl): PermissionTracker

    @Binds
    @Singleton
    fun bindNotificationSettingsTracker(impl: NotificationSettingsTrackerImpl): NotificationSettingsTracker

    @Binds
    @Singleton
    fun bindPrayerCalendarTracker(impl: PrayerCalendarTrackerImpl): PrayerCalendarTracker
}
