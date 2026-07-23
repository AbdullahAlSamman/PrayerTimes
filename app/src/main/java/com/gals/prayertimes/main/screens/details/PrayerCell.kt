package com.gals.prayertimes.main.screens.details


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.common.mappers.mapPrayerColor
import com.gals.prayertimes.common.mappers.mapUiPrayerName
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.ui.theme.ComponentStyles
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.utils.isPhoneInLandscape
import com.gals.prayertimes.utils.isTabletInPortrait

@Composable
fun PrayerCell(
    modifier: Modifier = Modifier,
    prayer: UiPrayerEntry,
    style: Style = Style,
) {
    val isTabletPortrait = isTabletInPortrait()
    val isPhoneLandscape = isPhoneInLandscape()
    val cellStyle = PrayerTimesTheme.styles.prayerCellStyle then
        PrayerTimesTheme.styles.darkTextStyle then
        ComponentStyles.getCellMinHeightStyle(isTabletPortrait)

    val styleState = remember { MutableStyleState(null) }
    Column(
        modifier = modifier
            .styleable(styleState, cellStyle, style)
            .background(
                color = mapPrayerColor(prayer = prayer.name),
                shape = MaterialTheme.shapes.small
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = mapUiPrayerName(prayerName = prayer.name),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 7.dp else 20.dp))

        Text(
            text = prayer.time,
            textAlign = TextAlign.Center
        )
    }
}