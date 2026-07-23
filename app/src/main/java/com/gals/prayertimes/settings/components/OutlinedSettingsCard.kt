package com.gals.prayertimes.settings.components

import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gals.prayertimes.ui.theme.PrayerTimesTheme

@Composable
internal fun OutlinedSettingsCard(
    modifier: Modifier = Modifier,
    style: Style = Style,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        modifier = modifier.styleable(remember { MutableStyleState(null) }, PrayerTimesTheme.styles.outlinedCardStyle, style),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        content()
    }
}
