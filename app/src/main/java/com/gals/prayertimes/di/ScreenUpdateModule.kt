package com.gals.prayertimes.di

import com.gals.prayertimes.model.TestViewModelScreenUpdater
import com.gals.prayertimes.model.ViewModelScreenUpdater
import com.gals.prayertimes.utils.ScreenUpdater
import com.gals.prayertimes.utils.TestScreenUpdater
import com.gals.prayertimes.utils.VMScreenUpdater
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScreenUpdateModule {

    @ViewModelScreenUpdater
    @Provides
    fun provideViewModelScreenUpdater(): ScreenUpdater = VMScreenUpdater()

    @TestViewModelScreenUpdater
    @Provides
    fun provideTestScreenUpdater(): ScreenUpdater = TestScreenUpdater()
}