package com.gals.prayertimes.settings.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.settings.screens.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography

data class UiMenuItem(
    @DrawableRes val icon: Int = 0,
    @StringRes val title: Int = 0,
    val navigateTo: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenuScreen(
    onBackClicked: () -> Unit,
    uiMenuItems: List<UiMenuItem>,
    textStyle: TextStyle = PrayerTypography.headlineMedium
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = stringResource(id = R.string.text_settings_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle
                    )
                },
                navigationIcon = {
                    NavigationBackArrow(
                        onBackAction = onBackClicked
                    )
                },
                actions = {/* no-op */ }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                content = {
                    items(uiMenuItems) { item ->
                        MenuItem(uiMenuItem = item)
                    }
                }
            )
        }
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun MenuItem(
    uiMenuItem: UiMenuItem,
    textStyle: TextStyle = PrayerTypography.headlineSmall
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .clickable { uiMenuItem.navigateTo() }
    )
    {
        Icon(
            modifier = Modifier
                .size(50.dp)
                .semantics { hideFromAccessibility() },
            painter = painterResource(id = uiMenuItem.icon),
            contentDescription = stringResource(R.string.menu_item_icon_content_description)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp),
            text = stringResource(id = uiMenuItem.title),
            style = textStyle
        )
    }
}

