package com.gals.prayertimes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.repository.remote.model.PrayerName
import com.gals.prayertimes.ui.theme.DarkTextStyle
import com.gals.prayertimes.utils.isPhoneInLandscape
import com.gals.prayertimes.utils.isTablet
import com.gals.prayertimes.utils.isTabletInPortrait
import com.gals.prayertimes.utils.nonScaledSp
import com.gals.prayertimes.utils.prayerColorMapper
import com.gals.prayertimes.utils.prayerNameMapper

/** Show single prayer with settings*/
@Composable
fun PrayerSingleView(
    modifier: Modifier = Modifier,
    prayer: Map.Entry<PrayerName, String>,
    textStyle: TextStyle = if (isTablet()) {
        DarkTextStyle.copy(fontSize = 32.nonScaledSp)
    } else {
        DarkTextStyle.copy(fontSize = 20.nonScaledSp)
    }
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = if(isTabletInPortrait()) 160.dp else 120.dp)
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp)
            .background(
                color = prayerColorMapper(prayer = prayer.key),
                shape = MaterialTheme.shapes.small
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(if (isPhoneInLandscape()) 5.dp else 15.dp))

        Text(
            text = prayerNameMapper(prayerName = prayer.key),
            style = textStyle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(if (isPhoneInLandscape()) 7.dp else 20.dp))

        Text(
            text = prayer.value,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}