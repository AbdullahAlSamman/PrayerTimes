package com.gals.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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

@Composable
fun PrayerDateBar(
    modifier: Modifier = Modifier,
    day: String,
    moonDate: String,
    sunDate: String,
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle = LightTextStyle,
    textTopPadding: Dp = 2.dp,
    textBottomPadding: Dp = 2.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(color = colorBackgroundViewDate)
    ) {
        Text(
            modifier = modifier
                .weight(1f)
                .padding(top = textTopPadding, bottom = textBottomPadding),
            text = day,
            textAlign = textAlign,
            style = textStyle
        )
        Text(
            modifier = modifier
                .weight(2f)
                .padding(top = textTopPadding, bottom = textBottomPadding),
            text = sunDate,
            textAlign = textAlign,
            style = textStyle
        )
        Text(
            modifier = modifier
                .padding(top = textTopPadding, bottom = textBottomPadding),
            text = stringResource(id = R.string.text_date_separator),
            textAlign = textAlign,
            style = textStyle
        )
        Text(
            modifier = modifier
                .weight(2f)
                .padding(top = textTopPadding, bottom = textBottomPadding),
            text = moonDate,
            textAlign = textAlign,
            style = textStyle
        )
    }
}