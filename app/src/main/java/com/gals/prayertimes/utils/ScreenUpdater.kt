package com.gals.prayertimes.utils

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ScreenUpdater {
    fun startTicks(delay: Long): Flow<Unit>
}

class VMScreenUpdater : ScreenUpdater {
    override fun startTicks(delay: Long): Flow<Unit> =
        flow {
            while (true) {
                Log.i("ngz_screen_updater", "tick at ${System.currentTimeMillis()}")
                emit(Unit)
                delay(delay)
            }
        }
}

class TestScreenUpdater : ScreenUpdater {
    override fun startTicks(delay: Long): Flow<Unit> = flow {
        delay(5)
    }
}