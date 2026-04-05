package com.gals.prayertimes.main.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.gals.prayertimes.common.UiPrayer
import com.gals.prayertimes.common.UiPrayerName

sealed class MainScreenUiState {
    data object Consent : MainScreenUiState()
    data object Loading : MainScreenUiState()
    data class Success(val uiPrayer: UiPrayer, val canShowAds: Boolean) : MainScreenUiState()
    data class Error(val message: String) : MainScreenUiState()
}

@Immutable
data class UiPrayerEntry(
    val name: UiPrayerName,
    val time: String
)

data class UiNextPrayer(
    @DrawableRes val backgroundImage: Int = 0,
    val settingsIconTint: Color = Color.Black,
    val nextPrayerName: String = "",
    val nextPrayerTime: String = "",
    val nextPrayerBanner: String = ""
)

data class UiDate(
    val dayName: String = "",
    val moonDate: String = "",
    val sunDate: String = ""
)