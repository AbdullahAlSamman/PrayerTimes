package com.gals.prayertimes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gals.prayertimes.R
import com.gals.prayertimes.model.UiMenuItem
import com.gals.prayertimes.ui.screens.NotificationScreen
import com.gals.prayertimes.ui.screens.PrayerScreen
import com.gals.prayertimes.ui.screens.PrivacyPolicyScreen
import com.gals.prayertimes.ui.screens.SettingsMenuScreen

@Composable
fun PrayerTimesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            PrayerScreen(onSettingsClicked = { navController.navigate(Menu.route) })
        }

        composable(route = Menu.route) {
            SettingsMenuScreen(
                onBackClicked = { navController.navigateUp() },
                uiMenuItems = listOf(
                    UiMenuItem(
                        icon = R.drawable.icon_notification_active,
                        title = R.string.text_settings_notifiaction,
                        navigateTo = { navController.navigate(Notification.route) }
                    ),
                    UiMenuItem(
                        icon = R.drawable.icon_privacy_policy,
                        title = R.string.text_settings_privacy_policy,
                        navigateTo = { navController.navigate(PrivacyPolicy.route) }
                    ),
                    UiMenuItem(
                        icon = R.drawable.icon_info,
                        title = R.string.text_settings_about_us,
                        navigateTo = {}
                    )
                )
            )
        }

        composable(route = PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onBackClicked = { navController.navigateUp() },
                webUri = stringResource(id = R.string.asset_url_privacy_policy)
            )
        }

        composable(route = Notification.route) {
            NotificationScreen(
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}