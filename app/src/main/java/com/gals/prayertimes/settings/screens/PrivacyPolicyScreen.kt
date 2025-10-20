package com.gals.prayertimes.settings.screens

import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.viewinterop.AndroidView
import com.gals.prayertimes.R
import com.gals.prayertimes.settings.screens.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTypography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PrivacyPolicyScreen(
    webUri: String,
    onBackClicked: () -> Unit,
    textStyle: TextStyle = PrayerTypography.headlineMedium
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
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
            AndroidView(
                modifier = Modifier.padding(innerPadding),
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        loadUrl(webUri)
                    }
                }
            )
        }
    )
}