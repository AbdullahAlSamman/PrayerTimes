package com.gals.prayertimes.main.screens

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.main.screens.details.PrayerDateBar
import com.gals.prayertimes.main.screens.details.PrayerHeader
import com.gals.prayertimes.main.screens.details.PrayerSingleView
import com.google.common.collect.ImmutableList

@Composable
internal fun PrayerPortraitScreen(
    prayers: ImmutableList<UiPrayerEntry>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    adBanner: @Composable BoxScope.(isMediumAdSize: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PrayerHeader(
            modifier = Modifier.fillMaxWidth(),
            imageScale = ContentScale.FillWidth,
            config = uiNextPrayer
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
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false,
            content = {
                prayers.forEach { prayer ->
                    item {
                        PrayerSingleView(prayer = prayer)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            adBanner(maxHeight >= 250.dp)
        }
    }
}