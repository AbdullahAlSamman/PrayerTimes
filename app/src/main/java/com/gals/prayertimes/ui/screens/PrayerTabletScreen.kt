package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.ui.components.PrayerDateBar
import com.gals.prayertimes.ui.components.PrayerHeader
import com.gals.prayertimes.ui.components.PrayerSingleView

@Composable
internal fun PrayerTabletScreen(
    innerPadding: PaddingValues,
    prayers: Map<PrayerName, String>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    onSettingsClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrayerHeader(
                config = uiNextPrayer,
                onSettingsClicked = onSettingsClicked
            )

            PrayerDateBar(
                day = uiDate.dayName,
                moonDate = uiDate.moonDate,
                sunDate = uiDate.sunDate
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(6),
            modifier = Modifier.weight(1f),
            contentPadding = innerPadding,
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                prayers.onEach { prayer ->
                    item {
                        PrayerSingleView(
                            modifier = Modifier.defaultMinSize(minWidth = 80.dp),
                            prayer = prayer
                        )
                    }
                }
            }
        )
    }
}