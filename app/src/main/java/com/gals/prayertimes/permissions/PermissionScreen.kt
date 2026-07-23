package com.gals.prayertimes.permissions

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.gals.prayertimes.R
import com.gals.prayertimes.permissions.model.PermissionType
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.utils.PrayerPreview
import com.gals.prayertimes.utils.upAPILevel33
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import timber.log.Timber
import com.gals.prayertimes.permissions.model.PermissionState as UiPermissionState


@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
object PermissionScreen {

    sealed class State {
        data object Loading : State()
        data class Content(
            val permissions: Map<PermissionType, UiPermissionState>
        ) : State()
    }

    @Composable
    operator fun invoke(
        onCloseClicked: () -> Unit,
        onFinishClicked: () -> Unit,
        viewModel: PermissionViewModel = hiltViewModel()
    ) {
        val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
        val permissionStates by viewModel.uiState.collectAsState()
        val notificationPermissionState =
            if (upAPILevel33) {
                rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
            } else null

        LaunchedEffect(lifecycleState) {
            if (lifecycleState == Lifecycle.State.RESUMED) {
                Timber.tag("permission").i("Permission Screen Resumed")
                viewModel.updatePermissions()
            }
        }

        PermissionScreenInternal(
            state = permissionStates,
            notificationPermissionState = notificationPermissionState,
            onClose = {
                viewModel.onClose()
                onCloseClicked()
            },
            onFinish = {
                viewModel.onFinish()
                onFinishClicked()
            },
            onRequestPermission = viewModel::requestPermission,
            onOpenSettings = viewModel::openSettings
        )
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
private fun PermissionScreenInternal(
    state: PermissionScreen.State,
    notificationPermissionState: PermissionState?,
    onClose: () -> Unit,
    onFinish: () -> Unit,
    onRequestPermission: (PermissionType) -> Unit,
    onOpenSettings: (PermissionType) -> Unit,
) {
    when (state) {
        PermissionScreen.State.Loading -> {
            Box(Modifier.fillMaxSize()) {
                ContainedLoadingIndicator(
                    modifier = Modifier
                        .styleable(
                            remember { MutableStyleState(null) },
                            PrayerTimesTheme.styles.loadingIndicatorLargeStyle
                        )
                        .align(Alignment.Center),
                    indicatorColor = colorBackgroundFajer,
                    containerColor = colorBackgroundIsha
                )
            }
        }

        is PermissionScreen.State.Content -> {
            val requirePermissions =
                state.permissions.filter { it.value == UiPermissionState.Required }.toList()
            val pagerState = rememberPagerState { requirePermissions.size }

            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onClose) {
                                Icon(
                                    modifier = Modifier.styleable(remember {
                                        MutableStyleState(
                                            null
                                        )
                                    }, PrayerTimesTheme.styles.closeIconStyle),
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = stringResource(id = R.string.content_descriptor_back_arrow)
                                )
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        if (requirePermissions.isNotEmpty()) {
                            HorizontalPager(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                state = pagerState
                            ) { page ->
                                PageContent(
                                    permission = requirePermissions[page].first,
                                    notificationPermissionState = notificationPermissionState,
                                    onRequestPermission = onRequestPermission,
                                    onOpenSettings = onOpenSettings
                                )
                            }

                            PageIndicator(
                                pagerState = pagerState,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.BottomCenter)
                            )

                        } else {
                            PermissionInfo(
                                modifier = Modifier.align(Alignment.Center),
                                icon = Icons.Filled.DoneAll,
                                iconDescription = "",
                                title = stringResource(R.string.text_permission_all_done),
                                buttons = {
                                    Button(onClick = onFinish) {
                                        Text(
                                            modifier = Modifier.styleable(remember {
                                                MutableStyleState(
                                                    null
                                                )
                                            }, PrayerTimesTheme.styles.buttonTextStyle),
                                            text = stringResource(R.string.text_permission_continue)
                                        )
                                    }
                                }
                            )
                        }
                    }
                })
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun PageContent(
    permission: PermissionType,
    notificationPermissionState: PermissionState?,
    onRequestPermission: (PermissionType) -> Unit,
    onOpenSettings: (PermissionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (permission) {
            PermissionType.Notification -> {
                notificationPermissionState?.let {
                    val status = it.status
                    PermissionInfo(
                        icon = Icons.Filled.NotificationsActive,
                        iconDescription = "",
                        title = stringResource(R.string.text_permission_notification_title),
                        description = stringResource(R.string.text_permission_notification_description),
                        buttons = {
                            when {
                                !status.shouldShowRationale && status != PermissionStatus.Granted -> {
                                    Button(onClick = { onOpenSettings(PermissionType.Notification) }) {
                                        Text(
                                            modifier = Modifier.styleable(remember {
                                                MutableStyleState(
                                                    null
                                                )
                                            }, PrayerTimesTheme.styles.buttonTextStyle),
                                            text = stringResource(R.string.text_permission_go_to_settings_button)
                                        )
                                    }
                                }

                                else -> {
                                    Button(onClick = { notificationPermissionState.launchPermissionRequest() }) {
                                        Text(
                                            modifier = Modifier.styleable(remember {
                                                MutableStyleState(
                                                    null
                                                )
                                            }, PrayerTimesTheme.styles.buttonTextStyle),
                                            text = stringResource(R.string.text_permission_request_button)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            PermissionType.BatteryOptimisation -> {
                PermissionInfo(
                    icon = Icons.Filled.BatterySaver,
                    iconDescription = "",
                    title = stringResource(R.string.text_permission_battery_title),
                    buttons = {
                        Button(onClick = { onRequestPermission(permission) }) {
                            Text(
                                modifier = Modifier.styleable(
                                    remember { MutableStyleState(null) },
                                    PrayerTimesTheme.styles.buttonTextStyle
                                ),
                                text = stringResource(R.string.text_permission_request_button)
                            )
                        }
                    }
                )
            }

            PermissionType.Alarm -> {
                PermissionInfo(
                    icon = Icons.Filled.AlarmOn,
                    iconDescription = "",
                    title = stringResource(R.string.text_permission_alarm_title),
                    buttons = {
                        Button(onClick = { onRequestPermission(permission) }) {
                            Text(
                                modifier = Modifier.styleable(
                                    remember { MutableStyleState(null) },
                                    PrayerTimesTheme.styles.buttonTextStyle
                                ),
                                text = stringResource(R.string.text_permission_request_button)
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PageIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
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
                    .styleable(
                        remember { MutableStyleState(null) },
                        PrayerTimesTheme.styles.pageIndicatorDotStyle
                    )
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun PermissionInfo(
    icon: ImageVector,
    iconDescription: String,
    title: String,
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Column(
        modifier = modifier.styleable(
            remember { MutableStyleState(null) },
            PrayerTimesTheme.styles.permissionInfoStyle
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier.styleable(
                remember { MutableStyleState(null) },
                PrayerTimesTheme.styles.permissionIconStyle
            ),
            imageVector = icon,
            contentDescription = iconDescription
        )
        Text(
            modifier = Modifier.styleable(
                remember { MutableStyleState(null) },
                PrayerTimesTheme.styles.headlineMediumStyle
            ),
            text = title
        )
        description?.let {
            Text(
                modifier = Modifier.styleable(
                    remember { MutableStyleState(null) },
                    PrayerTimesTheme.styles.bodyMediumStyle
                ),
                text = it,
                textAlign = TextAlign.Center
            )
        }
        buttons()
    }
}

@PrayerPreview
@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionScreenPreview() {
    val notificationPermissionState = if (upAPILevel33) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else null

    PrayerTimesTheme {
        PermissionScreenInternal(
            state = PermissionScreen.State.Content(
                permissions = mapOf(
                    PermissionType.Notification to UiPermissionState.Required,
                    PermissionType.BatteryOptimisation to UiPermissionState.Required,
                    PermissionType.Alarm to UiPermissionState.Required
                )
            ),
            notificationPermissionState = notificationPermissionState,
            onClose = {},
            onFinish = {},
            onRequestPermission = {},
            onOpenSettings = {}
        )
    }
}
