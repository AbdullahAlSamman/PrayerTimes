package com.gals.prayertimes.settings.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gals.prayertimes.R
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.mapUiPrayerName
import com.gals.prayertimes.settings.NotificationViewModel
import com.gals.prayertimes.settings.screens.components.NavigationBackArrow
import com.gals.prayertimes.settings.screens.components.RadioButtonItem
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
internal fun NotificationScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    textStyle: TextStyle = PrayerTypography.headlineMedium,
    viewModel: NotificationViewModel = hiltViewModel()
) { //TODO if any permissions are missing open permission screen
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
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
                        onBackAction = {
                            viewModel.submitChanges()
                            onBackClicked()
                        }
                    )
                },
                actions = {/* no-op */ }
            )
        },
        content = { innerPadding ->
            val uiSelectedRadio by viewModel.uiSelectedRadio.collectAsState()
            val uiSwitchState by viewModel.uiSwitchState.collectAsState()
            val uiPrayerSwitches by viewModel.uiSelectedPrayerAlarms.collectAsState()

            val isRadioItemSelected: (NotificationType) -> Boolean = { uiSelectedRadio == it }
            val onRadioSelectionChanged: (NotificationType) -> Unit =
                { viewModel.updateSelectedRadio(it) }
            val onSwitchSelectionChanged: (Boolean) -> Unit = { viewModel.updateSwitchState(it) }
            val onAlarmSelectionChanged: (UiPrayerName, Boolean) -> Unit =
                { name, value -> viewModel.updateSelectedAlarms(name, value) }

            BackHandler {
                viewModel.submitChanges()
                onBackClicked()
            }

            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(modifier = modifier.fillMaxWidth()) {
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
                Column(modifier = modifier) {
                    val items = NotificationType.entries.toTypedArray()
                    items.forEach { item ->
                        RadioButtonItem(
                            item = item,
                            isSelectedItem = isRadioItemSelected,
                            onSelectionChanged = onRadioSelectionChanged,
                            itemEnabled = uiSwitchState
                        )
                    }
                }

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.text_notification_selected_prayers),
                        style = PrayerTypography.titleLarge
                    )
                    UiPrayerName.entries.forEach { name ->
                        PrayerNotificationItem(
                            prayerName = name,
                            isSwitchChecked = uiPrayerSwitches[name] == true,
                            isSwitchEnabled = uiSwitchState,
                            onCheckedChange = { onAlarmSelectionChanged(name, it) }
                        )
                    }
                }
            }
        })
}

@Composable
internal fun PrayerNotificationItem(
    modifier: Modifier = Modifier,
    prayerName: UiPrayerName,
    isSwitchChecked: Boolean,
    isSwitchEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textStyle: TextStyle = PrayerTypography.titleMedium
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Switch(
            checked = isSwitchChecked,
            onCheckedChange = onCheckedChange,
            enabled = isSwitchEnabled
        )

        Text(
            text = mapUiPrayerName(prayerName),
            style = textStyle
        )
    }
}