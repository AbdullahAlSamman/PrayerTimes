package com.gals.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.repository.remote.model.SinglePrayer
import com.gals.prayertimes.ui.theme.DarkTextStyle
import com.gals.prayertimes.utils.prayerColorMapper
import com.gals.prayertimes.utils.prayerNameMapper

/** Show single prayer with settings*/
@Composable
fun PrayerSingleView(
    modifier: Modifier = Modifier,
    prayer: SinglePrayer,
    textStyle: TextStyle = DarkTextStyle
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp, top = 8.dp)
            .background(
                color = prayerColorMapper(prayer = prayer),
                shape = MaterialTheme.shapes.small
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = prayerNameMapper(prayerName = prayer.name),
            modifier = modifier.padding(top = 5.dp),
            style = textStyle
        )
        Text(text = prayer.time, modifier = modifier.padding(top = 20.dp), style = textStyle)
    }
}