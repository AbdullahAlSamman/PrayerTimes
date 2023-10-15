package com.gals.prayertimes.ui.components

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
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.gals.prayertimes.utils.getNotificationTypeText

@Composable
fun RadioButtonItem(
    item: NotificationType,
    isSelectedItem: (String) -> Boolean,
    onSelectionChanged: (String) -> Unit,
    textStyle: TextStyle = PrayerTypography.titleMedium,
    itemEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .selectable(
                selected = isSelectedItem(item.value),
                onClick = { onSelectionChanged(item.value) },
                role = Role.RadioButton
            )
            .padding(top = 4.dp)
            .fillMaxWidth()
    ) {
        RadioButton(
            enabled = itemEnabled,
            selected = isSelectedItem(item.value),
            onClick = { onSelectionChanged(item.value) })
        Text(
            text = getNotificationTypeText(notificationType = item),
            style = textStyle
        )
    }
}
