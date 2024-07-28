package com.gals.prayertimes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.ui.theme.LightTextStyle
import com.gals.prayertimes.utils.DynamicSizedText
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
    imageScale: ContentScale,
    onSettingsClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .paint(
                painterResource(id = config.backgroundImage),
                contentScale = imageScale
            )
    ) {
        val iconPadding = if (isTablet()) 32.dp else 16.dp
        Icon(
            modifier = Modifier
                .size(if (isTablet()) 80.dp else 48.dp)
                .align(Alignment.TopStart)
                .padding(
                    top = iconPadding,
                    start = iconPadding
                )
                .clickable {
                    onSettingsClicked()
                },
            tint = (if (isSystemInDarkTheme()) Color.White else Color.Black),
            painter = painterResource(id = R.drawable.icon_rounded_setting),
            contentDescription = stringResource(id = R.string.content_descriptor_settings_icon)
        )

        val textConfigPadding = if(isTablet()) 32.dp else 16.dp
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = textConfigPadding,
                    end = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DynamicSizedText(
                    text = config.nextPrayerBanner,
                    style = textStyle,
                    textAlign = TextAlign.Center
                )
                DynamicSizedText(
                    text = config.nextPrayerName,
                    style = textStyle,
                    textAlign = TextAlign.Center
                )
            }
            DynamicSizedText(
                text = config.nextPrayerTime,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
    }

}