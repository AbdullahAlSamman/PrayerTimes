package com.gals.prayertimes.main.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.ui.theme.LightTextStyle
import com.gals.prayertimes.utils.isTablet
import com.gals.prayertimes.utils.nonScaledSp

@Composable
fun PrayerHeader(
    modifier: Modifier = Modifier,
    config: UiNextPrayer,
    textStyle: TextStyle = if (isTablet()) {
        LightTextStyle.copy(fontSize = 36.nonScaledSp)
    } else {
        LightTextStyle.copy(fontSize = 20.nonScaledSp)
    },
    imageScale: ContentScale
) {
    Box(
        modifier = modifier
            .paint(
                painterResource(id = config.backgroundImage),
                contentScale = imageScale
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = if (isTablet()) 32.dp else 16.dp,
                    end = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = config.nextPrayerBanner,
                    style = textStyle,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = config.nextPrayerName,
                    style = textStyle,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = config.nextPrayerTime,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
    }

}