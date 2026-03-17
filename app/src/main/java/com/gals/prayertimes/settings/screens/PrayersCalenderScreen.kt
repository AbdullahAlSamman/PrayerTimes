package com.gals.prayertimes.settings.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.gals.prayertimes.R
import com.gals.prayertimes.settings.screens.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.timeNowInMilliseconds


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PrayersCalendarScreen(
    onBackClicked: () -> Unit,
    textStyle: TextStyle = PrayerTypography.headlineMedium
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = timeNowInMilliseconds()) //TODO use local date instance instead of millis if better
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
                actions = {})
        }
    ) { paddingValues ->
        //TODO fetch today prayer by default, and fetch from server on selected date changes
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
