package com.gals.prayertimes.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gals.prayertimes.R
import com.google.common.collect.ImmutableList

enum class NavigationMenuTarget {
    NOTIFICATIONS,
    PRIVACY_POLICY,
    CONSENT_FORM
}

data class NavigationDrawerMenuItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val navTarget: NavigationMenuTarget
)

val navigationMenuItems: ImmutableList<NavigationDrawerMenuItem> = ImmutableList.of(
    NavigationDrawerMenuItem(
        icon = R.drawable.ic_notification_active,
        title = R.string.text_settings_notifiaction,
        navTarget = NavigationMenuTarget.NOTIFICATIONS
    ),
    NavigationDrawerMenuItem(
        icon = R.drawable.ic_shield_toggle,
        title = R.string.text_settings_privacy_management,
        navTarget = NavigationMenuTarget.CONSENT_FORM
    ),
    NavigationDrawerMenuItem(
        icon = R.drawable.ic_policy,
        title = R.string.text_settings_privacy_policy,
        navTarget = NavigationMenuTarget.PRIVACY_POLICY
    )
)