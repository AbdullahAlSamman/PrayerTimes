package com.gals.prayertimes.main.screens.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.ComponentStyles
import com.gals.prayertimes.ui.theme.PrayerTimesTheme

@Composable
fun PrayerDateBar(
    modifier: Modifier = Modifier,
    day: String,
    moonDate: String,
    sunDate: String,
    textAlign: TextAlign = TextAlign.Center,
    style: Style = Style,
) {
    val dateBarStyle = PrayerTimesTheme.styles.dateBarStyle then
        PrayerTimesTheme.styles.lightTextStyle

    val styleState = remember { MutableStyleState(null) }

    Row(
        modifier = modifier.styleable(styleState, dateBarStyle, style)
    ) {
        val textModifier = Modifier.styleable(remember { MutableStyleState(null) }, ComponentStyles.dateBarTextPadding)

        Text(
            modifier = textModifier.padding(start = 4.dp),
            text = day,
            textAlign = textAlign
        )
        Text(
            modifier = textModifier.weight(3f),
            text = sunDate,
            textAlign = textAlign
        )
        Text(
            modifier = textModifier,
            text = stringResource(id = R.string.text_date_separator),
            textAlign = textAlign
        )
        Text(
            modifier = textModifier.weight(3f),
            text = moonDate,
            textAlign = textAlign
        )
    }
}