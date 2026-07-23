package com.gals.prayertimes.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object ComponentStyles {

    private val CellMinHeightTablet = 160.dp
    private val CellMinHeightDefault = 120.dp
    private val CellPaddingHorizontal = 2.dp

    private val HeaderPaddingBottomTablet = 32.dp
    private val HeaderPaddingBottomDefault = 16.dp
    private val HeaderPaddingEnd = 48.dp
    private val HeaderSpacedBy = 8.dp
    private val HeaderRowSpacedBy = 6.dp

    private val DateBarPaddingVertical = 2.dp

    private val DrawerButtonPaddingTablet = 32.dp
    private val DrawerButtonPaddingDefault = 16.dp
    private val DrawerButtonSizeTablet = 56.dp
    private val DrawerButtonSizeDefault = 48.dp

    private val StandardPadding = 16.dp

    private val PermissionIconSize = 128.dp
    private val LoadingIndicatorSizeStandard = 64.dp
    private val LoadingIndicatorSizeLarge = 164.dp
    private val CloseIconSize = 48.dp
    private val PageIndicatorDotSize = 12.dp
    private val PageIndicatorDotPadding = 4.dp

    val lightTextStyle = Style {
        textStyle(LightTextStyle)
    }

    val darkTextStyle = Style {
        textStyle(DarkTextStyle)
    }

    val titleMediumStyle = Style {
        textStyle(PrayerTypography.titleMedium)
    }

    val titleLargeStyle = Style {
        textStyle(PrayerTypography.titleLarge)
    }

    val headlineSmallStyle = Style {
        textStyle(PrayerTypography.headlineSmall)
    }

    val headlineMediumStyle = Style {
        textStyle(PrayerTypography.headlineMedium)
    }

    val buttonTextStyle = Style {
        fontWeight(FontWeight.Bold)
    }

    val bodyLargeStyle = Style {
        textStyle(PrayerTypography.bodyLarge)
    }

    val bodyMediumStyle = Style {
        textStyle(PrayerTypography.bodyMedium)
    }

    val prayerCellStyle = Style {
        fillWidth()
        contentPaddingStart(CellPaddingHorizontal)
        contentPaddingEnd(CellPaddingHorizontal)
    }

    val prayerHeaderStyle = Style {
        contentPaddingEnd(HeaderPaddingEnd)
    }

    val prayerHeaderSpacedBy: Arrangement.Vertical
        @Composable get() = Arrangement.spacedBy(HeaderSpacedBy)

    val prayerHeaderRowSpacedBy: Arrangement.Horizontal
        @Composable get() = Arrangement.spacedBy(HeaderRowSpacedBy)

    val dateBarStyle = Style {
        background(colorBackgroundViewDate)
    }

    val dateBarTextPadding = Style {
        contentPaddingTop(DateBarPaddingVertical)
        contentPaddingBottom(DateBarPaddingVertical)
    }

    val portraitScreenContainerStyle = Style {
        fillWidth()
    }

    val landscapeScreenContainerStyle = Style {
        fillSize()
    }

    val outlinedCardStyle = Style {
        contentPaddingStart(StandardPadding)
        contentPaddingEnd(StandardPadding)
    }

    val loadingIndicatorStyle = Style {
        size(LoadingIndicatorSizeStandard)
    }
    
    val loadingIndicatorLargeStyle = Style {
        size(LoadingIndicatorSizeLarge)
    }

    val errorScreenStyle = Style {
        fillSize()
        contentPaddingStart(StandardPadding)
        contentPaddingEnd(StandardPadding)
    }

    val permissionInfoStyle = Style {
        fillWidth()
    }

    val permissionIconStyle = Style {
        size(PermissionIconSize)
    }
    
    val closeIconStyle = Style {
        size(CloseIconSize)
    }

    val pageIndicatorDotStyle = Style {
        contentPadding(PageIndicatorDotPadding)
        size(PageIndicatorDotSize)
    }

    // Dynamic style getters for tablet/landscape logic at call site
    @Composable
    fun getCellMinHeightStyle(isTabletPortrait: Boolean) = Style {
        minHeight(if (isTabletPortrait) CellMinHeightTablet else CellMinHeightDefault)
    }

    @Composable
    fun getHeaderPaddingStyle(isTablet: Boolean) = Style {
        contentPaddingBottom(if (isTablet) HeaderPaddingBottomTablet else HeaderPaddingBottomDefault)
    }

    @Composable
    fun getDrawerButtonStyle(isTablet: Boolean) = Style {
        val padding = if (isTablet) DrawerButtonPaddingTablet else DrawerButtonPaddingDefault
        size(if (isTablet) DrawerButtonSizeTablet else DrawerButtonSizeDefault)
        contentPaddingTop(padding)
        contentPaddingStart(padding)
    }
}
