package com.gals.prayertimes.settings.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gals.prayertimes.R
import com.gals.prayertimes.common.mappers.mapUiPrayerName
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.settings.PrayersCalendarViewModel
import com.gals.prayertimes.settings.model.PrayersCalendarUiState
import com.gals.prayertimes.settings.screens.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.utils.isTabletInPortrait
import com.gals.prayertimes.utils.timeNowInMilliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import java.time.LocalDate


@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    FlowPreview::class,
    ExperimentalMaterial3ExpressiveApi::class
)
fun PrayersCalendarScreen(
    onBackClicked: () -> Unit,
    textStyle: TextStyle = PrayerTypography.headlineMedium,
    viewModel: PrayersCalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBackClicked()
    }


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = timeNowInMilliseconds(),
        yearRange = 2019..LocalDate.now().year
    )

    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }
            .debounce(1000)
            .collect { selectedDateMillis ->
                selectedDateMillis?.let { viewModel.fetchPrayersForDate(it) }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = stringResource(id = R.string.text_settings_prayers_calendar),
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
                actions = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                DatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false
                )
            }

            when (uiState) {
                PrayersCalendarUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ContainedLoadingIndicator(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center),
                            indicatorColor = colorBackgroundFajer,
                            containerColor = colorBackgroundIsha
                        )
                    }
                }

                is PrayersCalendarUiState.Success -> {
                    val state = uiState as PrayersCalendarUiState.Success
                    OutlinedCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.data.uiDate.sunDate,
                                style = PrayerTypography.headlineMedium,
                                modifier = Modifier.padding(8.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            )

                            Text(
                                text = state.data.uiDate.moonDate,
                                style = PrayerTypography.headlineSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    OutlinedCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(6),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            userScrollEnabled = false,
                            content = {
                                state.data.prayers.forEach { prayer ->
                                    item {
                                        SingleCell(prayer = prayer)
                                    }
                                }
                            }
                        )
                    }
                }

                is PrayersCalendarUiState.Error -> {
                    val state = uiState as PrayersCalendarUiState.Error
                    Text(
                        text = state.message,
                        style = PrayerTypography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleCell(
    prayer: UiPrayerEntry,
    textStyle: TextStyle = PrayerTypography.titleLarge
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = if (isTabletInPortrait()) 160.dp else 120.dp)
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = mapUiPrayerName(prayerName = prayer.name),
            style = textStyle,
            textAlign = TextAlign.Center
        )

        Text(
            text = prayer.time,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}