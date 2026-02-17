package com.gals.prayertimes.permissions.tracker

interface PermissionTracker {
    fun permissionOpen()
    fun permissionsGranted(isAllGranted: Boolean)
    fun permissionClose()
}