package com.gals.prayertimes.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.main.screens.details.PrayerDateBar
import com.gals.prayertimes.main.screens.details.PrayerHeader
import com.gals.prayertimes.main.screens.details.PrayerSingleCell
import com.gals.prayertimes.utils.isTablet
import com.google.common.collect.ImmutableList

@Composable
internal fun PrayerLandscapeScreen(
    prayers: ImmutableList<UiPrayerEntry>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    adBanner: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val isTabletInLandscape = isTablet()
        Layout(
            modifier = Modifier.fillMaxHeight(),
            content = {
                PrayerHeader(
                    imageScale = ContentScale.FillWidth,
                    config = uiNextPrayer
                )

                PrayerDateBar(
                    day = uiDate.dayName,
                    moonDate = uiDate.moonDate,
                    sunDate = uiDate.sunDate
                )
            }
        ) { measurables, constraints ->
            val headerMeasurable = measurables[0]
            val dateBarMeasurable = measurables[1]

            val dateBarHeight = if (isTabletInLandscape) 40.dp.roundToPx() else 25.dp.roundToPx()
            val headerHeight = (constraints.maxHeight - dateBarHeight)

            val headerConstraints = constraints.copy(
                minHeight = headerHeight,
                maxWidth = constraints.maxHeight
            )

            val dateBarConstraints = constraints.copy(
                minHeight = dateBarHeight,
                maxHeight = dateBarHeight,
                maxWidth = constraints.maxHeight
            )

            val headerPlaceable = headerMeasurable.measure(headerConstraints)
            val dateBarPlaceable = dateBarMeasurable.measure(dateBarConstraints)

            layout(constraints.maxHeight, constraints.maxHeight) {
                headerPlaceable.placeRelative(0, 0)
                dateBarPlaceable.placeRelative(0, constraints.maxHeight - dateBarHeight)
            }
        }

        LazyHorizontalGrid(
            modifier = Modifier,
            rows = GridCells.Fixed(3),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                prayers.onEach { prayer ->
                    item {
                        PrayerSingleCell(
                            modifier = Modifier.defaultMinSize(minWidth = 80.dp),
                            prayer = prayer
                        )
                    }
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            adBanner()
        }
    }
}