package com.gals.prayertimes.ui.screens

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.gals.prayertimes.model.PermissionState
import com.gals.prayertimes.ui.components.NavigationBackArrow
import com.gals.prayertimes.ui.components.RadioButtonItem
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.checkAPILevelForAlarms
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
                actions = {/* no-op */ }
            )
        },
        content = { innerPadding ->
            val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()

            val uiSelectedRadio by viewModel.uiSelectedRadio.collectAsState()
            val uiSwitchState by viewModel.uiSwitchState.collectAsState()
            val uiPermissionDialog by viewModel.uiPermissionState.collectAsState()

            val isRadioItemSelected: (String) -> Boolean = { uiSelectedRadio == it }
            val onRadioSelectionChanged: (String) -> Unit = { viewModel.updateSelectedRadio(it) }
            val onSwitchSelectionChanged: (Boolean) -> Unit = { viewModel.updateSwitchState(it) }

            CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                when (uiPermissionDialog) {
                    PermissionState.REQUESTED -> {
                        ShowMissingPermissionDialog(
                            updatePermissionState = viewModel::updatePermissionState,
                            requestPermission = viewModel::requestAlarmPermission
                        )
                    }

                    PermissionState.DENIED -> {
                        ShowPermissionDeniedDialog(viewModel::updatePermissionState)
                    }


                    else -> {/* no-op */
                    }
                }

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
                }

                LaunchedEffect(lifecycleState) {
                    when (lifecycleState) {
                        Lifecycle.State.RESUMED -> {
                            Log.i("ngz_notification", "Notification Screen: OnResume")
                            if (checkAPILevelForAlarms) {
                                when (viewModel.getPendingPermissions()) {
                                    PermissionState.PENDING -> {
                                        if (viewModel.isAlarmPermissionGranted()) {
                                            viewModel.updatePermissionState(PermissionState.GRANTED)
                                            viewModel.updateSwitchState(true)
                                        } else {
                                            viewModel.updatePermissionState(PermissionState.DENIED)
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
fun ShowMissingPermissionDialog(
    updatePermissionState: (PermissionState) -> Unit,
    requestPermission: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_permission_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_permission_dialog_message)) },
        confirmButton = {
            TextButton(
                onClick = {
                    updatePermissionState(PermissionState.REQUESTED)
                    requestPermission()
                }) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { updatePermissionState(PermissionState.DENIED) }
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_dismiss))
            }
        },
        onDismissRequest = { updatePermissionState(PermissionState.DENIED) }
    )
}

@Composable
fun ShowPermissionDeniedDialog(updatePermissionState: (PermissionState) -> Unit) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.text_notification_permission_dialog_denied_title)) },
        text = { Text(text = stringResource(id = R.string.text_notification_permission_dialog_denied_message)) },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = { updatePermissionState(PermissionState.NOT_REQUESTED) }
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_okay))
            }
        },
        onDismissRequest = { updatePermissionState(PermissionState.NOT_REQUESTED) }
    )
}