package com.gals.prayertimes.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gals.prayertimes.R
import com.gals.prayertimes.model.UiMenuItem
import com.gals.prayertimes.ui.screens.MainScreen
import com.gals.prayertimes.ui.screens.NotificationScreen
import com.gals.prayertimes.ui.screens.PrivacyPolicyScreen
import com.gals.prayertimes.ui.screens.SettingsMenuScreen

@Composable
fun PrayerTimesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            MainScreen(onSettingsClicked = { navController.navigate(Menu) })
        }

        composable<Menu> { backStackEntry ->
            SettingsMenuScreen(
                onBackClicked = { navController.navigateUp() },
                uiMenuItems = listOf(
                    UiMenuItem(
                        icon = R.drawable.icon_notification_active,
                        title = R.string.text_settings_notifiaction,
                        navigateTo = { navController.navigate(Notification) }
                    ),
                    UiMenuItem(
                        icon = R.drawable.icon_privacy_policy,
                        title = R.string.text_settings_privacy_policy,
                        navigateTo = { navController.navigate(PrivacyPolicy) }
                    )
                )
            )
        }

        composable<PrivacyPolicy> {
            PrivacyPolicyScreen(
                onBackClicked = { navController.navigateUp() },
                webUri = stringResource(id = R.string.asset_url_privacy_policy)
            )
        }

        composable<Notification> {
            NotificationScreen(
                modifier = Modifier.padding(horizontal = 16.dp),
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}