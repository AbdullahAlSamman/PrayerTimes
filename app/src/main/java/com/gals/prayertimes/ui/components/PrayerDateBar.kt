package com.gals.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.LightTextStyle
import com.gals.prayertimes.ui.theme.colorBackgroundViewDate
import com.gals.prayertimes.utils.DynamicSizedText
import com.gals.prayertimes.utils.isTablet
import com.gals.prayertimes.utils.nonScaledSp

@Composable
fun PrayerDateBar(
    modifier: Modifier = Modifier,
    day: String,
    moonDate: String,
    sunDate: String,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = if (isTablet()) {
        LightTextStyle.copy(fontSize = 36.nonScaledSp)
    } else {
        LightTextStyle.copy(
            fontSize = 20.nonScaledSp
        )
    },
    textTopPadding: Dp = 2.dp,
    textBottomPadding: Dp = 2.dp
) {
    Row(
        modifier = modifier.background(color = colorBackgroundViewDate)
    ) {
        val textModifier = modifier.padding(top = textTopPadding, bottom = textBottomPadding)

        DynamicSizedText(
            modifier = textModifier.padding(start = 4.dp),
            text = day,
            textAlign = textAlign,
            style = textStyle
        )
        DynamicSizedText(
            modifier = textModifier.weight(3f),
            text = sunDate,
            textAlign = textAlign,
            style = textStyle
        )
        DynamicSizedText(
            modifier = textModifier,
            text = stringResource(id = R.string.text_date_separator),
            textAlign = textAlign,
            style = textStyle
        )
        DynamicSizedText(
            modifier = textModifier.weight(3f),
            text = moonDate,
            textAlign = textAlign,
            style = textStyle
        )
    }
}