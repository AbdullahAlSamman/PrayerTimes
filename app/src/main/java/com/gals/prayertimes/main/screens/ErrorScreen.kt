package com.gals.prayertimes.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.PrayerTypography

@Composable
internal fun ErrorScreen(
    message: String,
    retry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = message,
            textAlign = TextAlign.Center,
            style = PrayerTypography.headlineSmall
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