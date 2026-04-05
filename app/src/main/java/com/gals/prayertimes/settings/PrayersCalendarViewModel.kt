package com.gals.prayertimes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.common.mappers.toPrayer
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.settings.model.PrayersCalendarUiState
import com.gals.prayertimes.utils.DateFormatter
import com.gals.prayertimes.utils.ResourceProvider
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
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    /*TODO: tracking, ads, tests, regions for VM, add outlined cards to notification screen*/
    private val _uiState = MutableStateFlow<PrayersCalendarUiState>(PrayersCalendarUiState.Loading)
    val uiState: StateFlow<PrayersCalendarUiState> = _uiState.asStateFlow()

    fun fetchPrayersForDate(selectedDateMillis: Long) {
        _uiState.update { PrayersCalendarUiState.Loading }
        val selectedDate = dateFormatter.formatMillisToDateString(selectedDateMillis)
        viewModelScope.launch {
            repository.getRemotePrayer(selectedDate)
                .map { it.toPrayer(resourceProvider, dateFormatter) }
                .catch { cause ->
                    _uiState.update { cause.toUiError() }
                }
                .collect { uiPrayer -> _uiState.update { PrayersCalendarUiState.Success(uiPrayer) } }
        }
    }

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
}