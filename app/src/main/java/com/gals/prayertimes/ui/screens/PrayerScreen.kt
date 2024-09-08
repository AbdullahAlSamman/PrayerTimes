package com.gals.prayertimes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.utils.isLandscape
import com.gals.prayertimes.utils.isTablet
import com.gals.prayertimes.viewmodel.MainViewModel

@Composable
fun PrayerScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiNextPrayer by viewModel.nextPrayer.collectAsState()

    Scaffold { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            LaunchedEffect(uiState) {
                if (uiState is UiState.Success) {
                    viewModel.startUiTicks()
                }
            }
            when (uiState) {
                is UiState.Error -> {
                    val state = uiState as UiState.Error
                    ErrorScreen(message = state.message, retry = viewModel::reload)
                }

                is UiState.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is UiState.Success -> {
                    val state = uiState as UiState.Success

                    if (isLandscape()) {
                        PrayerLandscapeScreen(
                            innerPadding = innerPadding,
                            prayers = state.uiPrayer.prayers,
                            uiNextPrayer = uiNextPrayer,
                            uiDate = state.uiPrayer.uiDate,
                            onSettingsClicked = onSettingsClicked
                        )
                        Log.i("ngz_screen", "isTablet: ${isTablet()}")
                    } else {
                        PrayerPortraitScreen(
                            innerPadding = innerPadding,
                            prayers = state.uiPrayer.prayers,
                            uiNextPrayer = uiNextPrayer,
                            uiDate = state.uiPrayer.uiDate,
                            onSettingsClicked = onSettingsClicked
                        )
                        Log.i("ngz_screen", "isTablet: ${isTablet()}")
                    }
                }
            }
        }
    }
}