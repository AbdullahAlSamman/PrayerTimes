package com.gals.prayertimes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.gals.prayertimes.R
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.ui.components.constraint.PrayerHeaderConstraint
import com.gals.prayertimes.ui.theme.LightTextStyle
import com.gals.prayertimes.utils.DynamicSizedText
import com.gals.prayertimes.utils.isTabletInLandscape
import com.gals.prayertimes.utils.nonScaledSp

@Composable
fun PrayerHeader(
    config: UiNextPrayer,
    onSettingsClicked: () -> Unit,
    textStyle: TextStyle = LightTextStyle.copy(fontSize = 20.nonScaledSp)
) {
    val imageModifier = if(isTabletInLandscape()) Modifier else Modifier.fillMaxWidth()
    ConstraintLayout(constraintSet = PrayerHeaderConstraint.header) {
        Image(
            modifier = imageModifier
                .layoutId("backgroundImage"),
            painter = painterResource(id = config.backgroundImage),
            contentDescription = stringResource(id = R.string.content_descriptor_background),
            contentScale = ContentScale.FillWidth
        )
        Icon(
            modifier = Modifier
                .layoutId("settingsButton")
                .clickable {
                    onSettingsClicked()
                },
            tint = (if (isSystemInDarkTheme()) Color.White else Color.Black),
            painter = painterResource(id = R.drawable.icon_rounded_setting),
            contentDescription = stringResource(id = R.string.content_descriptor_settings_icon)
        )
        ConstraintLayout(
            modifier = Modifier.layoutId("prayerRemainingText"),
            constraintSet = PrayerHeaderConstraint.row
        ) {
            DynamicSizedText(
                modifier = Modifier.layoutId("prayerRemainingString"),
                text = config.nextPrayerBanner,
                style = textStyle,
                textAlign = TextAlign.Center
            )
            DynamicSizedText(
                modifier = Modifier.layoutId("prayerNameString"),
                text = config.nextPrayerName,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
        DynamicSizedText(
            modifier = Modifier.layoutId("prayerRemainingTime"),
            text = config.nextPrayerTime,
            style = textStyle.copy(fontSize = 22.nonScaledSp),
            textAlign = TextAlign.Center
        )
    }
}