package com.gals.prayertimes.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.gals.prayertimes.ui.theme.PrayerTimesTheme

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
// Phone Portrait
@Preview(name = "1. Phone Portrait - Light", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "2. Phone Portrait - Dark", device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// Phone Landscape (Using the modern spec string)
@Preview(name = "3. Phone Landscape - Light", device = "spec:parent=pixel_5,orientation=landscape", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "4. Phone Landscape - Dark", device = "spec:parent=pixel_5,orientation=landscape", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
// Tablet
@Preview(name = "5. Tablet - Light", device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "6. Tablet - Dark", device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class PrayerPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Responsive App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello, Compose!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@PrayerPreview
@Composable
private fun PreviewScreenPreview() {
    PrayerTimesTheme {
        Surface {
            PreviewScreen()
        }
    }
}
