package com.gals.prayertimes.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.common.UiPrayerName
import com.gals.prayertimes.main.model.UiDate
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.main.model.UiPrayerEntry
import com.gals.prayertimes.main.screens.details.PrayerCell
import com.gals.prayertimes.main.screens.details.PrayerDateBar
import com.gals.prayertimes.main.screens.details.PrayerHeader
import com.gals.prayertimes.utils.PrayerContentPreviewTheme
import com.gals.prayertimes.utils.PrayerPreviewLandscape
import com.gals.prayertimes.utils.isTablet
import com.google.common.collect.ImmutableList

@Composable
internal fun PrayerLandscapeScreen(
    prayers: ImmutableList<UiPrayerEntry>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    adBanner: @Composable BoxScope.(maxAdHeight: Dp) -> Unit,
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
                        PrayerCell(
                            modifier = Modifier.defaultMinSize(minWidth = 80.dp),
                            prayer = prayer
                        )
                    }
                }
            }
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            adBanner(maxHeight)
        }
    }
}

@PrayerPreviewLandscape
@Composable
private fun PrayerScreenContentPreview() {
    PrayerContentPreviewTheme {
        PrayerLandscapeScreen(
            prayers = ImmutableList.of(
                UiPrayerEntry(UiPrayerName.FAJER, "05:00"),
                UiPrayerEntry(UiPrayerName.SUNRISE, "06:30"),
                UiPrayerEntry(UiPrayerName.DUHR, "12:00"),
                UiPrayerEntry(UiPrayerName.ASR, "15:30"),
                UiPrayerEntry(UiPrayerName.MAGHRIB, "18:00"),
                UiPrayerEntry(UiPrayerName.ISHA, "19:30"),
            ),
            uiNextPrayer = UiNextPrayer(
                backgroundImage = R.drawable.background_day,
                nextPrayerName = "المغرب",
                nextPrayerTime = "06:00",
                nextPrayerBanner = "02:30:00"
            ),
            uiDate = UiDate(
                dayName = "الاثنين",
                moonDate = "15 رمضان 1445",
                sunDate = "25 مارس 2024"
            ),
            adBanner = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Advertisement",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        )
    }
}