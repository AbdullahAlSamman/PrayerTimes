package com.gals.prayertimes.di

import com.gals.prayertimes.common.DefaultDispatcher
import com.gals.prayertimes.common.TestDispatcher
import com.gals.prayertimes.common.TestViewModelScreenUpdater
import com.gals.prayertimes.common.ViewModelScreenUpdater
import com.gals.prayertimes.utils.ScreenUpdater
import com.gals.prayertimes.utils.TestScreenUpdater
import com.gals.prayertimes.utils.VMScreenUpdater
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object ScreenUpdateModule {

    @ViewModelScreenUpdater
    @Provides
    fun provideViewModelScreenUpdater(
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): ScreenUpdater = VMScreenUpdater(dispatcher)

    @TestViewModelScreenUpdater
    @Provides
    fun provideTestScreenUpdater(
        @TestDispatcher dispatcher: CoroutineDispatcher
    ): ScreenUpdater = TestScreenUpdater(dispatcher)
}