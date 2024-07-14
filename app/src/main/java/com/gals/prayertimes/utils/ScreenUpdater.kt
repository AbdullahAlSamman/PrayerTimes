package com.gals.prayertimes.utils

import android.util.Log
import com.gals.prayertimes.model.DefaultDispatcher
import com.gals.prayertimes.model.TestDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                Log.i("ngz_screen_updater", "tick at ${System.currentTimeMillis()}")
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