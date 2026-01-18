package com.gals.prayertimes.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Policy
import androidx.compose.ui.graphics.vector.ImageVector
import com.gals.prayertimes.R

enum class NavigationMenuTarget {
    NOTIFICATIONS,
    PRIVACY_POLICY
}

data class NavigationDrawerMenuItem(
    val icon: ImageVector,
    @StringRes val title: Int,
    val navTarget: NavigationMenuTarget
)

val navigationMenuItems = listOf(
    NavigationDrawerMenuItem(
        icon = Icons.Filled.NotificationsActive,
        title = R.string.text_settings_notifiaction,
        navTarget = NavigationMenuTarget.NOTIFICATIONS
    ),
    NavigationDrawerMenuItem(
        icon = Icons.Filled.Policy,
        title = R.string.text_settings_privacy_policy,
        navTarget = NavigationMenuTarget.PRIVACY_POLICY
    )
)