package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.ui.components.PrayerDateBar
import com.gals.prayertimes.ui.components.PrayerHeader
import com.gals.prayertimes.ui.components.PrayerSingleView
import com.gals.prayertimes.utils.isTablet

@Composable
internal fun PrayerLandscapeScreen(
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
                .weight(if (isTablet()) 0.6f else 0.4f)
                .fillMaxHeight()
        ) {
            PrayerHeader(
                modifier = Modifier
                    .fillMaxHeight(0.93f)
                    .fillMaxWidth(),
                imageScale = ContentScale.FillWidth,
                config = uiNextPrayer,
                onSettingsClicked = onSettingsClicked
            )

            PrayerDateBar(
                modifier = Modifier.fillMaxHeight(),
                day = uiDate.dayName,
                moonDate = uiDate.moonDate,
                sunDate = uiDate.sunDate
            )
        }

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .fillMaxHeight()
        )

        LazyHorizontalGrid(
            modifier = Modifier.weight(0.4f),
            rows = GridCells.Fixed(6),
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