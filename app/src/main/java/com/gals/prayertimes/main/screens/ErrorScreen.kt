package com.gals.prayertimes.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.PrayerTimesTheme

@Composable
internal fun ErrorScreen(
    message: String,
    retry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val styleState = remember { MutableStyleState(null) }
    Column(
        modifier = modifier
            .styleable(styleState, PrayerTimesTheme.styles.errorScreenStyle),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .styleable(remember { MutableStyleState(null) }, PrayerTimesTheme.styles.headlineSmallStyle)
                .padding(horizontal = 16.dp),
            text = message,
            textAlign = TextAlign.Center
        )

        ElevatedButton(onClick = retry) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = stringResource(id = R.string.text_error_retry_button)
            )
            Text(text = stringResource(id = R.string.text_error_retry_button))
        }
    }
}