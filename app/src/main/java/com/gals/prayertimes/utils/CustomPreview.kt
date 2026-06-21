package com.gals.prayertimes.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.gals.prayertimes.settings.components.NavigationBackArrow
import com.gals.prayertimes.ui.theme.PrayerTimesTheme
import com.gals.prayertimes.ui.theme.PrayerTypography

// Phone Portrait
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone Portrait - Light",
    device = Devices.PHONE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
private annotation class PhoneLight

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone Portrait - Dark",
    device = Devices.PHONE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
private annotation class PhoneDark

// Phone Landscape
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone Landscape - Light",
    device = "spec:parent=pixel_10_pro_xl,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
private annotation class PhoneLandscapeLight

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone Landscape - Dark",
    device = "spec:parent=pixel_10_pro_xl,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
private annotation class PhoneLandscapeDark

// Tablet
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Tablet Landscape- Light",
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
private annotation class TabletLandscapeLight

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Tablet Landscape- Dark",
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
private annotation class TabletLandscapeDark

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Tablet - Light",
    device = "spec:parent=pixel_tablet,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
private annotation class TabletLight

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Tablet - Dark",
    device = "spec:parent=pixel_tablet,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
private annotation class TabletDark

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PhoneLandscapeLight
@PhoneLandscapeDark
@TabletLandscapeLight
@TabletLandscapeDark
annotation class PrayerPreviewLandscape

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PhoneLight
@PhoneDark
@TabletLight
@TabletDark
annotation class PrayerPreviewPortrait

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PrayerPreviewPortrait
@PrayerPreviewLandscape
annotation class PrayerPreview

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PrayerPreviewTheme(
    topAppBarTitle: String? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    PrayerTimesTheme {
        Scaffold(modifier = Modifier.fillMaxWidth(), topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = {
                    Text(
                        text = topAppBarTitle.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = PrayerTypography.headlineMedium
                    )
                },
                navigationIcon = {
                    NavigationBackArrow(
                        onBackAction = {}
                    )
                },
                actions = {/* no-op */ }
            )
        }) { innerPadding ->
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                content(innerPadding)
            }
        }
    }
}

@Composable
fun PrayerContentPreviewTheme(content: @Composable () -> Unit){
    PrayerTimesTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            content()
        }
    }
}