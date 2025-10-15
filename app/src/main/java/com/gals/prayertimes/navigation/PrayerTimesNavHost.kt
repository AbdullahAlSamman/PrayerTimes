package com.gals.prayertimes.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gals.prayertimes.R
import com.gals.prayertimes.main.MainScreen
import com.gals.prayertimes.navigation.PrayerTimesNavHost.NavDestination
import com.gals.prayertimes.permissions.PermissionScreen
import com.gals.prayertimes.settings.screens.NotificationScreen
import com.gals.prayertimes.settings.screens.PrivacyPolicyScreen
import com.gals.prayertimes.settings.screens.SettingsMenuScreen
import com.gals.prayertimes.settings.screens.UiMenuItem
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf


object PrayerTimesNavHost {
    @Serializable
    sealed class NavDestination {
        @Serializable
        data object Home : NavDestination()

        @Serializable
        data object Menu : NavDestination()

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
    operator fun invoke(
        navController: NavHostController,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = NavDestination.Home,
            modifier = modifier
        ) {
            composable<NavDestination.Home> {
                MainScreen(onSettingsClicked = { navController.navigate(NavDestination.Menu) })
            }

            composable<NavDestination.Menu> {
                SettingsMenuScreen(
                    onBackClicked = { navController.navigateUp() },
                    uiMenuItems = listOf(
                        UiMenuItem(
                            icon = R.drawable.icon_notification_active,
                            title = R.string.text_settings_notifiaction,
                            navigateTo = {
                                navController.navigateToGrantPermission(
                                    false, //TODO: check permission before navigation
                                    NavDestination.Notification
                                )
                            }
                        ),
                        UiMenuItem(
                            icon = R.drawable.icon_privacy_policy,
                            title = R.string.text_settings_privacy_policy,
                            navigateTo = { navController.navigate(NavDestination.PrivacyPolicy) }
                        )
                    )
                )
            }

            composable<NavDestination.PrivacyPolicy> {
                PrivacyPolicyScreen(
                    onBackClicked = { navController.navigateUp() },
                    webUri = stringResource(id = R.string.asset_url_privacy_policy)
                )
            }

            composable<NavDestination.Notification> {
                NotificationScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onBackClicked = { navController.navigateUp() }
                )
            }

            composable<NavDestination.Permission>(
                typeMap = mapOf(typeOf<NavDestination>() to CustomNavTypes.NavDestination)
            ) { backStackEntry ->
                val permissionRoute: NavDestination.Permission = backStackEntry.toRoute()
                PermissionScreen(
                    onBackClicked = { navController.navigateUp() },
                    onFinish = {
                        navController.navigate(permissionRoute.nextDestination) {
                            popUpTo<NavDestination.Permission> { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

private fun NavController.navigateToGrantPermission(
    areAllPermissionsGranted: Boolean,
    destination: NavDestination
) {
    if (areAllPermissionsGranted) {
        navigate(destination)
    } else {
        navigate(NavDestination.Permission(destination))
    }
}