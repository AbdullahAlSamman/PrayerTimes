package com.gals.prayertimes.utils

import com.gals.prayertimes.common.DefaultDispatcher
import com.gals.prayertimes.common.TestDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

interface ScreenUpdater {
    fun startTicks(delay: Long): Flow<Unit>
}

class VMScreenUpdater @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ScreenUpdater {
    override fun startTicks(delay: Long): Flow<Unit> =
        flow {
            while (true) {
                Timber.i("tick at ${System.currentTimeMillis()}")
                emit(Unit)
                delay(delay)
            }
        }.flowOn(dispatcher)
}

class TestScreenUpdater @Inject constructor(
    @TestDispatcher private val dispatcher: CoroutineDispatcher
) : ScreenUpdater {
    override fun startTicks(delay: Long): Flow<Unit> = flow<Unit> {
        delay(5)
    }.flowOn(dispatcher)
}