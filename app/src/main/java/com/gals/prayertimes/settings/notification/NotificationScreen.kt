package com.gals.prayertimes.settings.notification

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.gals.prayertimes.R
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.mapUiPrayerName
import com.gals.prayertimes.settings.components.NavigationBackArrow
import com.gals.prayertimes.settings.components.OutlinedSettingsCard
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.PrayerPreview
import com.gals.prayertimes.utils.PrayerPreviewTheme
import com.gals.prayertimes.utils.isLandscape
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
internal fun NotificationScreen(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = PrayerTypography.headlineMedium,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
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

            NotificationContent(
                modifier = modifier,
                innerPadding = innerPadding,
                uiSwitchState = uiSwitchState,
                onSwitchSelectionChanged = onSwitchSelectionChanged,
                isRadioItemSelected = isRadioItemSelected,
                onRadioSelectionChanged = onRadioSelectionChanged,
                uiPrayerSwitches = uiPrayerSwitches.toImmutableMap(),
                onAlarmSelectionChanged = onAlarmSelectionChanged
            )
        })
}

@Composable
private fun NotificationContent(
    innerPadding: PaddingValues,
    modifier: Modifier,
    uiSwitchState: Boolean,
    onSwitchSelectionChanged: (Boolean) -> Unit,
    isRadioItemSelected: (NotificationType) -> Boolean,
    onRadioSelectionChanged: (NotificationType) -> Unit,
    uiPrayerSwitches: ImmutableMap<UiPrayerName, Boolean>,
    onAlarmSelectionChanged: (UiPrayerName, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .then(
                if (isLandscape()) Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp) else Modifier.fillMaxWidth()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        NotificationToggle(
            modifier = modifier,
            uiSwitchState = uiSwitchState,
            onSwitchSelectionChanged = onSwitchSelectionChanged
        )

        NotificationTypeRadioGroup(
            modifier = modifier,
            uiSwitchState = uiSwitchState,
            isRadioItemSelected = isRadioItemSelected,
            onRadioSelectionChanged = onRadioSelectionChanged
        )

        SelectedPrayersToggleGroup(
            modifier = modifier,
            uiPrayerSwitches = uiPrayerSwitches,
            uiSwitchState = uiSwitchState,
            onAlarmSelectionChanged = onAlarmSelectionChanged
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun NotificationTypeRadioGroup(
    modifier: Modifier,
    isRadioItemSelected: (NotificationType) -> Boolean,
    onRadioSelectionChanged: (NotificationType) -> Unit,
    uiSwitchState: Boolean
) {
    OutlinedSettingsCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.text_notification_selected_type),
                style = PrayerTypography.titleLarge
            )
            val items = NotificationType.entries.toTypedArray()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = if (isLandscape()) 4 else 2
            ) {
                items.forEach { item ->
                    RadioButtonItem(
                        modifier = Modifier.weight(1f),
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

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SelectedPrayersToggleGroup(
    modifier: Modifier,
    uiPrayerSwitches: ImmutableMap<UiPrayerName, Boolean>,
    uiSwitchState: Boolean,
    onAlarmSelectionChanged: (UiPrayerName, Boolean) -> Unit
) {
    OutlinedSettingsCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.text_notification_selected_prayers),
                style = PrayerTypography.titleLarge
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UiPrayerName.entries.forEach { name ->
                    PrayerNotificationItem(
                        modifier = Modifier.weight(1f),
                        prayerName = name,
                        isSwitchChecked = uiPrayerSwitches[name] == true,
                        isSwitchEnabled = uiSwitchState,
                        onCheckedChange = { onAlarmSelectionChanged(name, it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationToggle(
    modifier: Modifier,
    uiSwitchState: Boolean,
    onSwitchSelectionChanged: (Boolean) -> Unit
) {
    OutlinedSettingsCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
    }
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

@PrayerPreview
@Composable
private fun NotificationContentAppPreview() {
    PrayerPreviewTheme(topAppBarTitle = stringResource(R.string.text_settings_notifiaction)) { innerPadding ->
        NotificationContent(
            innerPadding = innerPadding + PaddingValues(16.dp),
            modifier = Modifier,
            uiSwitchState = true,
            onSwitchSelectionChanged = {},
            isRadioItemSelected = { it == NotificationType.TONE },
            onRadioSelectionChanged = {},
            uiPrayerSwitches = UiPrayerName.entries.associateWith { true }.toImmutableMap(),
            onAlarmSelectionChanged = { _, _ -> }
        )
    }
}
