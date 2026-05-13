package com.gals.prayertimes.settings.calendar

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.ads.ConsentManager
import com.gals.prayertimes.ads.model.AdPlacement
import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.common.mappers.toPrayer
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.settings.calendar.model.PrayersCalendarUiState
import com.gals.prayertimes.settings.calendar.tracker.PrayerCalendarTracker
import com.gals.prayertimes.utils.DateFormatter
import com.gals.prayertimes.utils.ResourceProvider
import com.google.android.gms.ads.AdView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayersCalendarViewModel @Inject constructor(
    private val repository: PrayersRepository,
    private val dateFormatter: DateFormatter,
    private val resourceProvider: ResourceProvider,
    private val tracker: PrayerCalendarTracker,
    private val adsManager: AdsManager,
    private val consentManager: ConsentManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<PrayersCalendarUiState>(PrayersCalendarUiState.Loading)
    val uiState: StateFlow<PrayersCalendarUiState> = _uiState.asStateFlow()

    //region UI operations
    fun fetchPrayersForDate(selectedDateMillis: Long) {
        _uiState.update { PrayersCalendarUiState.Loading }
        viewModelScope.launch {
            dateFormatter.formatMillisToDateString(selectedDateMillis).let { selectedDate ->
                tracker.selectedDate(selectedDate)
                repository.getRemotePrayer(selectedDate)
                    .catch { cause ->
                        _uiState.update { cause.toUiError() }
                    }
                    .map { it.toPrayer(resourceProvider, dateFormatter) }
                    .collect { uiPrayer ->
                        _uiState.update { PrayersCalendarUiState.Success(uiPrayer) }
                    }
            }
        }
    }
    //endregion

    //region Ads
    fun requestAdBanner(context: Context, adSize: Dp): AdView {
        val adView = adsManager.requestBannerAd(
            context = context,
            maxAdHeight = adSize,
            adPlacement = AdPlacement.PrayerCalendar
        )
        adsManager.loadAd(adView)
        return adView
    }

    val canShowAds
        get() = consentManager.canRequestAds
    //endregion

    //region Private Methods
    private fun Throwable.toUiError() =
        when (this) {
            is ConnectivityException ->
                PrayersCalendarUiState.Error(
                    resourceProvider.getString(R.string.text_error_check_internet)
                )


            is ServerException ->
                PrayersCalendarUiState.Error(
                    resourceProvider.getString(R.string.text_error_server_no_data)
                )


            else -> {
                PrayersCalendarUiState.Error(
                    resourceProvider.getString(R.string.text_error_server_down)
                )
            }
        }
//endregion
}