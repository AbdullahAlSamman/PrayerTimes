package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
internal fun PrayerPortraitScreen(
    innerPadding: PaddingValues,
    prayers: Map<PrayerName, String>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    onSettingsClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        PrayerHeader(
            modifier = Modifier.fillMaxHeight(if (isTablet()) .62f else .46f),
            imageScale = ContentScale.FillHeight,
            config = uiNextPrayer,
            onSettingsClicked = onSettingsClicked
        )

        Spacer(modifier = Modifier.height(4.dp))

        PrayerDateBar(
            day = uiDate.dayName,
            moonDate = uiDate.moonDate,
            sunDate = uiDate.sunDate
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            userScrollEnabled = false,
            content = {
                prayers.forEach { prayer ->
                    item {
                        PrayerSingleView(prayer = prayer)
                    }
                }
            }
        )
    }
}