package com.gals.prayertimes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.model.UiPrayerName
import com.gals.prayertimes.model.mappers.mapUiPrayerName
import com.gals.prayertimes.ui.theme.PrayerTypography

@Composable
internal fun PrayerNotificationItem(
    modifier: Modifier = Modifier,
    prayerName: UiPrayerName,
    isSwitchChecked: Boolean,
    isSwitchEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textStyle: TextStyle = PrayerTypography.titleMedium,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Switch(
            checked = isSwitchChecked,
            onCheckedChange = onCheckedChange,
            enabled = isSwitchEnabled
        )

        Text(
            text = mapUiPrayerName(prayerName),
            style = textStyle
        )
    }
}