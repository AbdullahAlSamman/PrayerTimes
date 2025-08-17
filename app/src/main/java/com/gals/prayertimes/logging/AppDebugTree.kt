package com.gals.prayertimes.logging

import timber.log.Timber
import javax.inject.Inject

class AppDebugTree @Inject constructor(
    private val globalMessagePrefix: String
) : Timber.DebugTree() {

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        val prefixedMessage = "$globalMessagePrefix: $message"
        super.log(priority, prefixedMessage, *args)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val prefixedMessage = "$globalMessagePrefix: $message"
        super.log(priority, tag, prefixedMessage, t)
    }
}