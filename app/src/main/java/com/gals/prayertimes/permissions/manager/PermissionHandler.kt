package com.gals.prayertimes.permissions.manager

/**
 * An interface for handling different types of permissions required by the application.
 */
interface PermissionHandler {
    /**
     * Checks if the permission is currently granted.
     * @return true if granted, false otherwise.
     */
    fun isGranted(): Boolean

    /**
     * Opens the relevant system settings screen for the user to grant the permission.
     */
    fun requestPermission(){/* no-op */}

    /**
     * Launches the permission request, if applicable.
     */
    fun openSettings(){/* no-op */}
}