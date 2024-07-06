package com.gals.prayertimes.model

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class TestDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ViewModelScreenUpdater

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class TestViewModelScreenUpdater