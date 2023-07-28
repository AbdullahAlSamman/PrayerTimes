package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.ui.components.PrayerDateBar
import com.gals.prayertimes.ui.components.PrayerHeader
import com.gals.prayertimes.ui.components.PrayerSingleView
import com.gals.prayertimes.viewmodel.MainViewModel

@Composable
fun PrayerScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiPrayers by viewModel.uiPrayers.collectAsState()
    val uiNextPrayer by viewModel.uiNextPrayer.collectAsState()
    val uiDateInfo by viewModel.uiDateInfo.collectAsState()
    val uiIsRamadan by viewModel.uiIsRamadan.collectAsState()

    Scaffold { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                when (uiState) {
                    is UiState.Error -> {
                        val state = uiState as UiState.Error
                        ErrorScreen(message = state.message, retry = viewModel::retryRequest)
                    }

                    is UiState.Loading -> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }

                    is UiState.Success -> {
                        PrayerHeader(
                            isRamadan = uiIsRamadan,
                            config = uiNextPrayer,
                            onSettingsClicked = onSettingsClicked
                        )
                        PrayerDateBar(
                            day = uiDateInfo.dayName,
                            moonDate = uiDateInfo.moonDate,
                            sunDate = uiDateInfo.sunDate
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(6),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = innerPadding,
                            userScrollEnabled = false,
                            content = {
                                items(uiPrayers.prayers.size) { index ->
                                    val item = uiPrayers.prayers[index]
                                    PrayerSingleView(
                                        prayer = item
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}