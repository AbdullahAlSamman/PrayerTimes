package com.gals.prayertimes.main.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha
import com.gals.prayertimes.utils.PrayerPreview

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        ContainedLoadingIndicator(
            modifier = Modifier
                .styleable(remember { MutableStyleState(null) }, PrayerTimesTheme.styles.loadingIndicatorLargeStyle)
                .align(Alignment.Center),
            indicatorColor = colorBackgroundFajer,
            containerColor = colorBackgroundIsha
        )

        Image(
            modifier = Modifier
                .styleable(remember { MutableStyleState(null) }, PrayerTimesTheme.styles.loadingIndicatorStyle)
                .align(alignment = Alignment.Center)
                .zIndex(1f),
            painter = painterResource(R.drawable.ic_haya_notification),
            contentDescription = stringResource(R.string.loading_screen_icon_content_description)
        )
    }
}

@PrayerPreview
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}