package com.gals.prayertimes.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClicked: () -> Unit,
    webUri: String,
    textStyle: TextStyle = PrayerTypography.headlineMedium
) {
    val state = rememberWebViewState(url = webUri)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                title = {
                    Text(
                        text = stringResource(id = R.string.text_settings_privacy_policy),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle
                    )
                },
                navigationIcon = {
                    NavigationBackArrow(
                        onBackAction = onBackClicked
                    )
                },
                actions = {})
        },
        content = { innerPadding ->
            WebView(
                modifier = Modifier.padding(innerPadding),
                state = state
            )
        }
    )
}