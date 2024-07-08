package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
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
    val uiNextPrayer by viewModel.nextPrayer.collectAsState()

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
                        ErrorScreen(message = state.message, retry = viewModel::reload)
                    }

                    is UiState.Loading -> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }

                    is UiState.Success -> {
                        val state = uiState as UiState.Success
                        PrayerHeader(
                            config = uiNextPrayer,
                            onSettingsClicked = onSettingsClicked
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        PrayerDateBar(
                            day = state.prayer.uiDate.dayName,
                            moonDate = state.prayer.uiDate.moonDate,
                            sunDate = state.prayer.uiDate.sunDate
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(6),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = innerPadding,
                            userScrollEnabled = false,
                            content = {
                                state.prayer.prayers.forEach { prayer ->
                                    item {
                                        PrayerSingleView(prayer = prayer)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}