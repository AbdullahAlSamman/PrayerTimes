package com.gals.prayertimes.settings.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gals.prayertimes.R
import com.gals.prayertimes.ads.utils.AdBanner
import com.gals.prayertimes.common.UiPrayer
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.common.mappers.mapUiPrayerName
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.settings.calendar.model.PrayersCalendarUiState
import com.gals.prayertimes.settings.components.NavigationBackArrow
import com.gals.prayertimes.settings.components.OutlinedSettingsCard
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.utils.PrayerPreview
import com.gals.prayertimes.utils.PrayerPreviewTheme
import com.gals.prayertimes.utils.isTabletInPortrait
import com.gals.prayertimes.utils.timeNowInMilliseconds
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

@Composable
@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
fun PrayersCalendarScreen(
    onBackClicked: () -> Unit,
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
            .debounce(250.milliseconds)
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
                        style = PrayerTypography.headlineMedium
                    )
                },
                navigationIcon = { NavigationBackArrow(onBackAction = onBackClicked) },
                actions = {}
            )
        }
    ) { paddingValues ->
        PrayersCalendarContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            datePickerState = datePickerState,
            adBanner = { maxAdHeight ->
                if (viewModel.canShowAds) {
                    AdBanner(
                        modifier = Modifier.align(Alignment.Center),
                        factory = { context ->
                            viewModel.requestAdBanner(
                                context = context,
                                adSize = maxAdHeight
                            )
                        }
                    )
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PrayersCalendarContent(
    datePickerState: DatePickerState,
    uiState: PrayersCalendarUiState,
    adBanner: @Composable BoxScope.(maxAdHeight: Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO consider landscape design
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedSettingsCard {
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
                Box(modifier = Modifier.defaultMinSize(minHeight = 216.dp)) {
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
                val state = uiState.data
                OutlinedSettingsCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.uiDate.sunDate,
                            style = PrayerTypography.headlineMedium,
                            modifier = Modifier.padding(8.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        )

                        Text(
                            text = state.uiDate.moonDate,
                            style = PrayerTypography.headlineSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                OutlinedSettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.prayers.forEach { prayer ->
                            SingleCell(prayer = prayer)
                        }
                    }
                }
            }

            is PrayersCalendarUiState.Error -> {
                OutlinedSettingsCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 216.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(alignment = Alignment.Center),
                            text = uiState.message,
                            textAlign = TextAlign.Center,
                            style = PrayerTypography.bodyMedium
                        )
                    }
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            adBanner(maxHeight)
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

@PrayerPreview
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
private fun PrayersCalendarContentAppPreview() {
    PrayerPreviewTheme(stringResource(R.string.text_settings_prayers_calendar)) { innerPadding ->
        PrayersCalendarContent(
            modifier = Modifier.padding(innerPadding),
            uiState = PrayersCalendarUiState.Success(
                data = UiPrayer(
                    uiDate = UiDate(
                        dayName = "Monday",
                        moonDate = "15 Shawwal 1445",
                        sunDate = "24 April 2024"
                    ),
                    prayers = ImmutableList.of(
                        UiPrayerEntry(UiPrayerName.FAJER, "04:30"),
                        UiPrayerEntry(UiPrayerName.SUNRISE, "06:00"),
                        UiPrayerEntry(UiPrayerName.DUHR, "12:30"),
                        UiPrayerEntry(UiPrayerName.ASR, "16:00"),
                        UiPrayerEntry(UiPrayerName.MAGHRIB, "19:00"),
                        UiPrayerEntry(UiPrayerName.ISHA, "20:30")
                    )
                )
            ),
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = 1713916800000L
            ),
            adBanner = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Advertisement",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        )
    }
}

@PrayerPreview
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
private fun PrayersCalendarContentErrorPreview() {
    PrayerPreviewTheme(stringResource(R.string.text_settings_prayers_calendar)) { innerPadding ->
        PrayersCalendarContent(
            modifier = Modifier.padding(innerPadding),
            uiState = PrayersCalendarUiState.Error(
                message = stringResource(R.string.text_error_server_down)
            ),
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = 1713916800000L
            ),
            adBanner = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Advertisement",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        )
    }
}