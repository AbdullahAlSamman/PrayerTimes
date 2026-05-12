package com.gals.prayertimes.settings.notification

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.common.NotificationType
import com.gals.prayertimes.common.mappers.mapNotificationTypeText
import com.gals.prayertimes.ui.theme.PrayerTypography

@Composable
fun RadioButtonItem(
    item: NotificationType,
    itemEnabled: Boolean,
    isSelectedItem: (NotificationType) -> Boolean,
    onSelectionChanged: (NotificationType) -> Unit,
    textStyle: TextStyle = PrayerTypography.titleMedium
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .selectable(
                selected = isSelectedItem(item),
                onClick = { onSelectionChanged(item) },
                role = Role.RadioButton
            )
            .padding(top = 4.dp)
            .fillMaxWidth()
    ) {
        RadioButton(
            enabled = itemEnabled,
            selected = isSelectedItem(item),
            onClick = { onSelectionChanged(item) })
        Text(
            text = mapNotificationTypeText(notificationType = item),
            style = textStyle
        )
    }
}
