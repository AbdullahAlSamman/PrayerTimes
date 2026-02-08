package com.gals.prayertimes.main.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.gals.prayertimes.common.UiPrayerName

sealed class UiState {
    data object Consent : UiState()
    data object Loading : UiState()
    data class Success(val uiPrayer: UiPrayer, val canShowAds: Boolean) : UiState()
    data class Error(val message: String) : UiState()
}

@Immutable
data class UiPrayer(
    var uiDate: UiDate = UiDate(),
    var prayers: Map<UiPrayerName, String> = emptyMap()
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