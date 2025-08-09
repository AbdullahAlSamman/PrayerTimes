package com.gals.prayertimes.ui.screens

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gals.prayertimes.R
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.model.UiPrayerName
import com.gals.prayertimes.model.UiPermissionState
import com.gals.prayertimes.ui.components.NavigationBackArrow
import com.gals.prayertimes.ui.components.PrayerNotificationItem
import com.gals.prayertimes.ui.components.RadioButtonItem
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.upAPILevel31
import com.gals.prayertimes.utils.upAPILevel33
import com.gals.prayertimes.viewmodel.NotificationScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
internal fun NotificationScreen(
    modifier: Modifier = Modifier,
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
            val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
            val context = LocalContext.current

            val uiSelectedRadio by viewModel.uiSelectedRadio.collectAsState()
            val uiSwitchState by viewModel.uiSwitchState.collectAsState()
            val uiPrayerSwitches by viewModel.uiSelectedPrayerAlarms.collectAsState()
            val uiAlarmPermissionDialog by viewModel.uiPermissionState.collectAsState()

            val isRadioItemSelected: (NotificationType) -> Boolean = { uiSelectedRadio == it }
            val onRadioSelectionChanged: (NotificationType) -> Unit =
                { viewModel.updateSelectedRadio(it) }
            val onSwitchSelectionChanged: (Boolean) -> Unit = { viewModel.updateSwitchState(it) }
            val onAlarmSelectionChanged: (UiPrayerName, Boolean) -> Unit =
                { name, value -> viewModel.updateSelectedAlarms(name, value) }
            val showRationaleDialog = remember { mutableStateOf(false) }
            val showGoToSettingsDialog = remember { mutableStateOf(false) }

            val notificationPermissionState: PermissionState? = if (upAPILevel33) {
                rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
            } else null

            BackHandler {
                viewModel.submitChanges()
                onBackClicked()
            }

            CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                when (uiAlarmPermissionDialog) {
                    UiPermissionState.REQUESTED -> {
                        ShowExactAlarmPermissionDialog(
                            updatePermissionState = viewModel::updatePermissionState,
                            requestPermission = viewModel::requestExactAlarmPermission
                        )
                    }

                    UiPermissionState.DENIED -> {
                        ShowPermissionDeniedDialog(viewModel::updatePermissionState)
                    }

                    else -> {/* no-op */
                    }
                }

                notificationPermissionState?.let { permissionState ->
                    LaunchedEffect(permissionState.status, lifecycleState) {
                        if (lifecycleState == Lifecycle.State.RESUMED && !permissionState.status.isGranted) {
                            if (permissionState.status.shouldShowRationale) {
                                showGoToSettingsDialog.value = false
                                showRationaleDialog.value = true
                            } else {
                                showRationaleDialog.value = false
                                showGoToSettingsDialog.value = true
                            }
                        } else if (permissionState.status.isGranted) {
                            showRationaleDialog.value = false
                            showGoToSettingsDialog.value = false
                        }
                    }

                    if (showRationaleDialog.value) {
                        ShowNotificationPermissionDialog(
                            requestPermission = {
                                permissionState.launchPermissionRequest()
                                showRationaleDialog.value = false
                            },
                            onDismissRequest = {
                                showRationaleDialog.value = false
                                showGoToSettingsDialog.value = true
                            }
                        )
                    }

                    if (showGoToSettingsDialog.value) {
                        ShowPermissionPermanentlyDeniedDialog(
                            onDismissRequest = { showGoToSettingsDialog.value = false },
                            onGoToSettingsClicked = {
                                showGoToSettingsDialog.value = false
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                    .apply {
                                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) { /* no-op */
                                }
                            }
                        )
                    }
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

                LaunchedEffect(lifecycleState) {
                    when (lifecycleState) {
                        Lifecycle.State.RESUMED -> {
                            Log.i("ngz_notification", "Notification Screen: OnResume")
                            if (upAPILevel31) {
                                when (viewModel.getPendingPermissions()) {
                                    UiPermissionState.PENDING -> {
                                        if (viewModel.isAlarmPermissionGranted()) {
                                            viewModel.updatePermissionState(UiPermissionState.GRANTED)
                                            viewModel.updateSwitchState(true)
                                        } else {
                                            viewModel.updatePermissionState(UiPermissionState.DENIED)
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        }

                        else -> {/* no-op */
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ShowExactAlarmPermissionDialog(
    updatePermissionState: (UiPermissionState) -> Unit,
    requestPermission: () -> Unit,
    dismissPermission: () -> Unit = { updatePermissionState(UiPermissionState.DENIED) }
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_alarm_permission_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_alarm_permission_dialog_message)) },
        confirmButton = {
            TextButton(
                onClick = {
                    updatePermissionState(UiPermissionState.REQUESTED)
                    requestPermission()
                }) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissPermission
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_dismiss))
            }
        },
        onDismissRequest = dismissPermission
    )
}

@Composable
private fun ShowNotificationPermissionDialog(
    requestPermission: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_permission_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_alarm_permission_dialog_message)) },
        confirmButton = {
            TextButton(onClick = { requestPermission() }) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_dismiss))
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun ShowPermissionDeniedDialog(updatePermissionState: (UiPermissionState) -> Unit) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_alarm_permission_dialog_denied_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_alarm_permission_dialog_denied_message)) },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = { updatePermissionState(UiPermissionState.NOT_REQUESTED) }
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_okay))
            }
        },
        onDismissRequest = { updatePermissionState(UiPermissionState.NOT_REQUESTED) }
    )
}

@Composable
private fun ShowPermissionPermanentlyDeniedDialog(
    onDismissRequest: () -> Unit,
    onGoToSettingsClicked: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_permission_denied_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_permission_denied_message)) },
        confirmButton = {
            TextButton(onClick = onGoToSettingsClicked) {
                Text(text = stringResource(id = R.string.text_notification_permission_go_to_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_dismiss))
            }
        },
        onDismissRequest = onDismissRequest
    )
}
