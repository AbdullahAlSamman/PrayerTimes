package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.model.ViewModelScreenUpdater
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.DateConfig
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.model.UiDate
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.ScreenUpdater
import com.gals.prayertimes.utils.getDayName
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toPrayer
import com.gals.prayertimes.utils.toTimePrayer
import com.gals.prayertimes.utils.toUiDate
import com.gals.prayertimes.utils.toUiNextPrayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ViewModelScreenUpdater private val screenUpdater: ScreenUpdater,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val calculation: PrayerCalculation,
    private val formatter: Formatter
) : ViewModel() {
    private var todayPrayers = Prayer()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _uiPrayers = MutableStateFlow(Prayer())
    private val _uiNextPrayer = MutableStateFlow(UiNextPrayer())
    private val _uiDate = MutableStateFlow(UiDate())

    val uiState: StateFlow<UiState> = _uiState
    val uiPrayers: StateFlow<Prayer> = _uiPrayers.asStateFlow()
    val nextPrayer: StateFlow<UiNextPrayer> = _uiNextPrayer.asStateFlow()
    val uiDate: StateFlow<UiDate> = _uiDate.asStateFlow()

    init {
        startLoading()
        screenUpdater.startTicks(delay = TICKS_DELAY, initialDelay = TICKS_INITIAL_DELAY)
            .onEach {
                updateScreenStates()
            }.launchIn(viewModelScope)
    }

    /**retry method to call from ui*/
    fun retryRequest() {
        startLoading()
    }

    /**update all flows related to ui*/
    private fun updateScreenStates() = try {
        Log.i(
            "isDayChanged",
            "${calculation.isDayChanged(todayPrayers.sDate)}"
        )

        if (calculation.isDayChanged(todayPrayers.sDate)) {
            startLoading()
        }

        updatePrayersState()
        updateNextPrayerState()
        updateDateState()
    } catch (e: Exception) {
        Log.i(
            "Flow updates",
            "error: ${e.message.toString()}"
        )
        _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_down)) }
    }

    /**update the time to next prayer*/
    private fun updateNextPrayerState() {
        _uiNextPrayer.update {
            calculation.calculateNextPrayerInfo(
                currentPrayer = todayPrayers.toTimePrayer(),
                moonDate = todayPrayers.mDate
            ).toUiNextPrayer()
        }
    }

    /** Build text for dates*/
    private fun updateDateState() {
        val calendar = Calendar.getInstance()
        _uiDate.update {
            DateConfig(
                dayName = resourceProvider.getString(getDayName(calendar[Calendar.DAY_OF_WEEK])),
                moonDate = formatter.formatDateText(todayPrayers.mDate, false),
                sunDate = formatter.formatDateText(todayPrayers.sDate, true)
            ).toUiDate()
        }
    }

    /**Update prayers flow*/
    private fun updatePrayersState() {
        _uiPrayers.update { todayPrayers }
    }

    /**Method to initial loading flow*/
    private fun startLoading() {
        viewModelScope.launch {
            repository.fetchComposePrayer(getTodayDate())
                .catch { cause ->
                    when (cause) {
                        is ConnectivityException -> {
                            _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_check_internet)) }
                        }

                        else -> {
                            _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_down)) }
                        }
                    }
                }
                .map { prayer ->
                    prayer.toPrayer()
                }
                .collect { prayers ->
                    prayers.let { composePrayers ->
                        todayPrayers = composePrayers
                        updateScreenStates()
                        _uiState.update { UiState.Success }
                    }
                }
        }
    }

    companion object {
        const val STRING_DATE_SEPARATOR = "."
        const val TICKS_INITIAL_DELAY: Long = 5_000
        const val TICKS_DELAY: Long = 25_000
    }
}