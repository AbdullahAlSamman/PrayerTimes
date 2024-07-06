package com.gals.prayertimes.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ScreenUpdater {
    fun startTicks(delay: Long, initialDelay: Long): Flow<Unit>
}

class VMScreenUpdater : ScreenUpdater {
    override fun startTicks(delay: Long, initialDelay: Long): Flow<Unit> =
        flow {
            delay(initialDelay)
            while (true) {
                emit(Unit)
                delay(delay)
            }
        }
}

class TestScreenUpdater : ScreenUpdater {
    override fun startTicks(delay: Long, initialDelay: Long): Flow<Unit> = flow {
        delay(delay)
    }

}

