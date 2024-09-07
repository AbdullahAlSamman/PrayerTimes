package com.gals.prayertimes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.gals.prayertimes.repository.remote.model.PrayerName

@Immutable
data class Prayer(
    var uiDate: UiDate = UiDate(),
    var prayers: Map<PrayerName, String> = emptyMap()
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

data class MenuItem(
    @DrawableRes val icon: Int = 0,
    @StringRes val title: Int = 0,
    val navigateTo: () -> Unit = {}
)

enum class PermissionState {
    PENDING,
    REQUESTED,
    GRANTED,
    DENIED,
    NOT_REQUESTED
}