package com.gals.prayertimes.permissions

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gals.prayertimes.R
import com.gals.prayertimes.utils.upAPILevel33
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

enum class UiPermissionState {
    PENDING,
    REQUIRED,
    GRANTED,
    DENIED,
    NOT_REQUIRED
}

enum class UiPermission {
    NOTIFICATION,
    BATTERY_OPTIMIZATION,
    ALARM
}

data class Permission(
    val key: UiPermission,
    val value: UiPermissionState
)

@Composable
@OptIn(ExperimentalPermissionsApi::class)
internal fun PermissionScreen(
    viewModel: PermissionViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onFinish: () -> Unit
) {
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val permissionStates by viewModel.uiPermissionStates.collectAsState()
    val context = LocalContext.current
    val showRationaleDialog = remember { mutableStateOf(false) }
    val showGoToSettingsDialog = remember { mutableStateOf(false) }
    val notificationPermissionState: PermissionState? = if (upAPILevel33) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else null
    val requiredPermissions = permissionStates.map { Permission(it.key, it.value) }
    val pagerState = rememberPagerState(pageCount = { requiredPermissions.size })

    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) { page ->
        Box {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable(onClick = onBackClicked)
                    .padding(16.dp),
                imageVector = Icons.Outlined.Close,
                contentDescription = ""
            )
            PageContent(
                modifier = Modifier.align(Alignment.Center),
                permission = requiredPermissions[page]
            )
            PageIndicator(
                modifier = Modifier.align(Alignment.BottomCenter),
                pagerState = pagerState
            )
        }
    }
}

@Composable
private fun PageContent(permission: Permission, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        when (permission.key) {
            UiPermission.NOTIFICATION -> {
                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = ""
                )
            }

            UiPermission.BATTERY_OPTIMIZATION -> {
                Icon(
                    imageVector = Icons.Filled.BatterySaver,
                    contentDescription = ""
                )
            }

            UiPermission.ALARM -> {
                Icon(
                    imageVector = Icons.Filled.AlarmOn,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PageIndicator(modifier: Modifier = Modifier, pagerState: PagerState) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)
            )
        }
    }
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
                    updatePermissionState(UiPermissionState.REQUIRED)
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
                onClick = { updatePermissionState(UiPermissionState.NOT_REQUIRED) }
            ) {
                Text(text = stringResource(id = R.string.text_notification_permission_dialog_okay))
            }
        },
        onDismissRequest = { updatePermissionState(UiPermissionState.NOT_REQUIRED) }
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

/*
CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
    when (permissionStates) {
        UiPermissionState.REQUIRED -> {
            ShowExactAlarmPermissionDialog(
                updatePermissionState = viewModel::updatePermissionState,
                requestPermission = viewModel::requestExactAlarmPermission
            )
        }

        UiPermissionState.DENIED -> {
            ShowPermissionDeniedDialog(viewModel::updatePermissionState)
        }

        else -> {*/
/* no-op *//*

        }
    }

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                Timber.i("Notification Screen: OnResume")
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

            else -> {*/
/* no-op *//*

            }
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
                    } catch (_: Exception) { */
/* no-op *//*

                    }
                }
            )
        }
    }
}*/
