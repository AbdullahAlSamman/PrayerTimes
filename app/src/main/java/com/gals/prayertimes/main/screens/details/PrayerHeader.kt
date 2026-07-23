package com.gals.prayertimes.main.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.ui.theme.ComponentStyles
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.utils.isTablet

@Composable
fun PrayerHeader(
    config: UiNextPrayer,
    imageScale: ContentScale,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val isTabletDevice = isTablet()
    val headerStyle = PrayerTimesTheme.styles.prayerHeaderStyle then
        PrayerTimesTheme.styles.lightTextStyle then
        ComponentStyles.getHeaderPaddingStyle(isTabletDevice)

    val styleState = remember { MutableStyleState(null) }

    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(config.backgroundImage),
            contentScale = imageScale,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .styleable(styleState, headerStyle, style),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = ComponentStyles.prayerHeaderSpacedBy
        ) {
            Row(
                horizontalArrangement = ComponentStyles.prayerHeaderRowSpacedBy
            ) {
                Text(
                    text = config.nextPrayerBanner,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = config.nextPrayerName,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = config.nextPrayerTime,
                textAlign = TextAlign.Center
            )
        }
    }
}