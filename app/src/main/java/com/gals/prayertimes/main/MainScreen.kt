package com.gals.prayertimes.main

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.gals.prayertimes.BuildConfig
import com.gals.prayertimes.R
import com.gals.prayertimes.main.model.UiState
import com.gals.prayertimes.main.screens.ConsentScreen
import com.gals.prayertimes.main.screens.ErrorScreen
import com.gals.prayertimes.main.screens.LoadingScreen
import com.gals.prayertimes.main.screens.PrayerLandscapeScreen
import com.gals.prayertimes.main.screens.PrayerPortraitScreen
import com.gals.prayertimes.navigation.NavigationDrawerMenuItem
import com.gals.prayertimes.navigation.NavigationMenuTarget
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.isLandscape
import com.gals.prayertimes.utils.isTablet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    uiNavigationMenuItems: List<NavigationDrawerMenuItem>,
    onNavigationMenuItemClick: (navTarget: NavigationMenuTarget, arePermissionsGranted: Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiNextPrayer by viewModel.nextPrayer.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        val activity = LocalActivity.current
        LaunchedEffect(activity) {
            activity?.let {
                val consentResult = viewModel.requestConsentIfRequired(activity)
                if (consentResult != null) {
                    Timber.e("consentResponse: Failed code:${consentResult.errorCode} message: ${consentResult.message}")
                }
                viewModel.startLoading()
            }
        }

        LaunchedEffect(uiState) {
            if (uiState is UiState.Success) {
                viewModel.startUiTicks()
            }
        }
        when (uiState) {
            UiState.Consent -> {
                ConsentScreen()
            }

            UiState.Loading -> {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }

            is UiState.Error -> {
                val state = uiState as UiState.Error
                ErrorScreen(message = state.message, retry = viewModel::reload)
            }

            is UiState.Success -> {
                val state = uiState as UiState.Success
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    gesturesEnabled = true,
                    drawerState = drawerState,
                    drawerContent = {
                        NavigationDrawerContent(
                            items = uiNavigationMenuItems,
                            coroutineScope = scope,
                            onNavigationMenuItemClick = onNavigationMenuItemClick,
                            drawerState = drawerState,
                            arePermissionsGranted = viewModel.areAllPermissionsGranted(),
                            resetConsent = { viewModel.resetConsent() }
                        )
                    }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        DrawerMenuButton(
                            modifier = Modifier.align(Alignment.TopStart),
                            scope = scope,
                            drawerState = drawerState
                        )
                        if (isLandscape()) {
                            PrayerLandscapeScreen(
                                prayers = state.uiPrayer.prayers,
                                uiNextPrayer = uiNextPrayer,
                                uiDate = state.uiPrayer.uiDate
                            )
                            Timber.i("isTablet: ${isTablet()}")
                        } else {
                            PrayerPortraitScreen(
                                prayers = state.uiPrayer.prayers,
                                uiNextPrayer = uiNextPrayer,
                                uiDate = state.uiPrayer.uiDate
                            )
                            Timber.i("isTablet: ${isTablet()}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    items: List<NavigationDrawerMenuItem>,
    coroutineScope: CoroutineScope,
    onNavigationMenuItemClick: (NavigationMenuTarget, Boolean) -> Unit,
    drawerState: DrawerState,
    arePermissionsGranted: Boolean,
    resetConsent: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(250.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.text_settings_title),
            style = PrayerTypography.titleLarge
        )
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(item.title),
                        style = PrayerTypography.titleMedium
                    )
                },
                selected = false,
                onClick = {
                    coroutineScope.launch {
                        onNavigationMenuItemClick(item.navTarget, arePermissionsGranted)
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title)
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    resetConsent()
                },
            textAlign = TextAlign.Center,
            text = "(${BuildConfig.VERSION_CODE})${BuildConfig.VERSION_NAME}",
            style = PrayerTypography.bodySmall
        )
    }
}

@Composable
private fun DrawerMenuButton(
    modifier: Modifier,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    val iconPadding = if (isTablet()) 32.dp else 16.dp
    IconButton(
        modifier = modifier
            .size(if (isTablet()) 56.dp else 48.dp)
            .padding(top = iconPadding, start = iconPadding)
            .semantics(true) {}
            .zIndex(1f),
        onClick = {
            scope.launch {
                drawerState.apply { if (isClosed) open() else close() }
            }
        }
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = Icons.Filled.Menu,
            contentDescription = stringResource(id = R.string.content_descriptor_settings_drawer)
        )
    }
}