package com.gals.prayertimes.settings.calendar.model

import com.gals.prayertimes.common.UiPrayer


sealed class PrayersCalendarUiState {
    data object Loading : PrayersCalendarUiState()
    data class Success(val data: UiPrayer) : PrayersCalendarUiState()
    data class Error(val message: String) : PrayersCalendarUiState()
}
