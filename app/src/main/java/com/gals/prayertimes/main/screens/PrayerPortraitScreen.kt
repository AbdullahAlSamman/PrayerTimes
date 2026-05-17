package com.gals.prayertimes.main.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.gals.prayertimes.utils.PrayerPreviewPortrait
import com.google.common.collect.ImmutableList

@Composable
internal fun PrayerPortraitScreen(
    prayers: ImmutableList<UiPrayerEntry>,
    uiNextPrayer: UiNextPrayer,
    uiDate: UiDate,
    adBanner: @Composable BoxScope.(maxAdHeight: Dp) -> Unit,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            prayers.forEach { prayer ->
                Box(modifier = Modifier.weight(1f)) {
                    PrayerCell(prayer = prayer)
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            adBanner(maxHeight)
        }
    }
}

@PrayerPreviewPortrait
@Composable
private fun PrayerScreenContentPreview() {
    PrayerContentPreviewTheme {
        PrayerPortraitScreen(
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
