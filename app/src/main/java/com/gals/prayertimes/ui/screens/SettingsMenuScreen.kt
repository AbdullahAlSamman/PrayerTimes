package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.gals.prayertimes.R
import com.gals.prayertimes.model.MenuItem
import com.gals.prayertimes.ui.components.MenuItem
import com.gals.prayertimes.ui.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenuScreen(
    onBackClicked: () -> Unit,
    menuItems: List<MenuItem>,
    textStyle: TextStyle = PrayerTypography.headlineMedium
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
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
                actions = {})
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                content = {
                    items(menuItems) { item ->
                        MenuItem(menuItem = item)
                    }
                })
        }
    )
}