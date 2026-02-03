package com.gals.prayertimes.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.gals.prayertimes.R
import com.gals.prayertimes.main.MainScreen
import com.gals.prayertimes.navigation.PrayerTimesNavHost.NavDestination
import com.gals.prayertimes.permissions.PermissionScreen
import com.gals.prayertimes.settings.screens.NotificationScreen
import com.gals.prayertimes.settings.screens.PrivacyPolicyScreen
import kotlinx.serialization.Serializable

object PrayerTimesNavHost {
    @Serializable
    sealed class NavDestination : NavKey {
        @Serializable
        data object Home : NavDestination()

        @Serializable
        data object PrivacyPolicy : NavDestination()

        @Serializable
        data object Notification : NavDestination()

        @Serializable
        data class Permission(
            val nextDestination: NavDestination
        ) : NavDestination()
    }

    @Composable
    operator fun invoke() {
        val navigationState = rememberNavigationState(
            startRoute = NavDestination.Home,
            topLevelRoutes = setOf(
                NavDestination.Home,
                NavDestination.Notification,
                NavDestination.PrivacyPolicy
            )
        )
        val navigator = remember { Navigator(navigationState) }

        val entryProvider = entryProvider<NavKey> {
            entry<NavDestination.Home> {
                MainScreen(
                    uiNavigationMenuItems = navigationMenuItems,
                    onNavigationMenuItemClick = { target, arePermissionsGranted ->
                        when (target) {
                            NavigationMenuTarget.NOTIFICATIONS -> {
                                navigator.navigateToGrantPermission(
                                    arePermissionsGranted,
                                    NavDestination.Notification
                                )
                            }

                            NavigationMenuTarget.PRIVACY_POLICY -> {
                                navigator.navigate(NavDestination.PrivacyPolicy)
                            }

                            NavigationMenuTarget.CONSENT_FORM -> {/* no-op */
                            }
                        }
                    }
                )
            }

            entry<NavDestination.PrivacyPolicy> {
                PrivacyPolicyScreen(
                    onBackClicked = { navigator.goBack() },
                    webUri = stringResource(id = R.string.asset_url_privacy_policy)
                )
            }

            entry<NavDestination.Notification> {
                NotificationScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onBackClicked = { navigator.goBack() }
                )
            }

            entry<NavDestination.Permission> { key ->
                PermissionScreen(
                    onBackClicked = { navigator.goBack() },
                    onFinish = {
                        navigator.navigateReplacing(key.nextDestination)
                    }
                )
            }
        }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            sceneStrategy = remember { DialogSceneStrategy() }
        )
    }
}

private fun Navigator.navigateToGrantPermission(
    areAllPermissionsGranted: Boolean,
    destination: NavDestination
) {
    if (areAllPermissionsGranted) {
        navigate(destination)
    } else {
        navigate(NavDestination.Permission(destination))
    }
}
