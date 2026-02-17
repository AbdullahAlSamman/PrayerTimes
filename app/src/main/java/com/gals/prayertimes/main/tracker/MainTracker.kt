package com.gals.prayertimes.main.tracker

interface MainTracker {
    fun consent(isGranted: Boolean)
    fun consentError(code: String, message: String)
    fun loading()
    fun reload()
    fun error(error: String)
    fun settingsOpen()
    fun navigationMenuItemOpen(navTarget: String)
}