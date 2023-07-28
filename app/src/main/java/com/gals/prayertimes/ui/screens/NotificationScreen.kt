package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.ui.components.NavigationBackArrow
import com.gals.prayertimes.ui.components.RadioButtonItem
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.viewmodel.NotificationScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClicked: () -> Unit,
    textStyle: TextStyle = PrayerTypography.headlineMedium,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                title = {
                    Text(
                        text = stringResource(id = R.string.text_settings_notifiaction),
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
            val uiSelectedRadio by viewModel.uiSelectedRadio.collectAsState()
            val uiSwitchState by viewModel.uiSwitchState.collectAsState()

            val isRadioItemSelected: (String) -> Boolean = { uiSelectedRadio == it }
            val onRadioSelectionChanged: (String) -> Unit = { viewModel.updateSelectedRadio(it) }
            val onSwitchSelectionChanged: (Boolean) -> Unit = { viewModel.updateSwitchState(it) }

            CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    {
                        Text(
                            modifier = Modifier
                                .weight(3f)
                                .align(Alignment.CenterVertically),
                            text = stringResource(id = R.string.text_settings_switch_title),
                            style = PrayerTypography.titleLarge
                        )
                        Switch(
                            modifier = Modifier.weight(1f),
                            checked = uiSwitchState,
                            onCheckedChange = onSwitchSelectionChanged
                        )
                    }
                    Column(modifier = Modifier.padding(8.dp)) {
                        val items = NotificationType.values()
                        items.forEach { item ->
                            RadioButtonItem(
                                item = item,
                                isSelectedItem = isRadioItemSelected,
                                onSelectionChanged = onRadioSelectionChanged,
                                itemEnabled = uiSwitchState
                            )
                        }
                    }
                }
            }
        }
    )
}