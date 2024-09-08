package com.gals.prayertimes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.gals.prayertimes.R
import com.gals.prayertimes.model.UiMenuItem
import com.gals.prayertimes.ui.theme.PrayerTypography

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuItem(
    uiMenuItem: UiMenuItem,
    textStyle: TextStyle = PrayerTypography.headlineSmall
) {
    CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .clickable { uiMenuItem.navigateTo.invoke() }
        )
        {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .semantics { invisibleToUser() },
                painter = painterResource(id = uiMenuItem.icon),
                contentDescription = stringResource(R.string.menu_item_icon_content_description)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp),
                text = stringResource(id = uiMenuItem.title),
                style = textStyle
            )
        }
    }
}