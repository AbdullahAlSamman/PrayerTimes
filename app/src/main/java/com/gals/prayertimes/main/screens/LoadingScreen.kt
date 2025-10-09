package com.gals.prayertimes.main.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.theme.colorBackgroundFajer
import com.gals.prayertimes.ui.theme.colorBackgroundIsha

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        ContainedLoadingIndicator(
            modifier = Modifier
                .size(164.dp)
                .align(Alignment.Center),
            indicatorColor = colorBackgroundFajer,
            containerColor = colorBackgroundIsha
        )

        Image(
            modifier = Modifier
                .size(64.dp)
                .align(alignment = Alignment.Center)
                .zIndex(1f),
            painter = painterResource(R.drawable.ic_haya_notification),
            contentDescription = stringResource(R.string.loading_screen_icon_content_description)
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}