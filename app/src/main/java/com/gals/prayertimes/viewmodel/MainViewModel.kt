package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.model.Prayer
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.model.config.DateConfig
import com.gals.prayertimes.model.config.NextPrayerConfig
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.getDayName
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toPrayer
import com.gals.prayertimes.utils.toTimePrayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val calculation: PrayerCalculation,
    private val formatter: Formatter
) : ViewModel() {
    private var todayPrayers = Prayer()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _uiPrayers = MutableStateFlow(Prayer())
    private val _uiNextPrayer = MutableStateFlow(NextPrayerConfig())
    private val _uiDateInfo = MutableStateFlow(DateConfig())
    private val _uiIsRamadan = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = _uiState
    val uiPrayers: StateFlow<Prayer> = _uiPrayers.asStateFlow()
    val uiNextPrayer: StateFlow<NextPrayerConfig> = _uiNextPrayer.asStateFlow()
    val uiDateInfo: StateFlow<DateConfig> = _uiDateInfo.asStateFlow()
    val uiIsRamadan: StateFlow<Boolean> = _uiIsRamadan.asStateFlow()

    init {
        startLoading()
        startTicks(delay = 25_000, initialDelay = 5_000)
            .onEach {
                updateScreenFlows()
            }.launchIn(viewModelScope)
    }

    /**retry method to call from ui*/
    fun retryRequest() {
        startLoading()
    }

    /**update all flows related to ui*/
    private fun updateScreenFlows() = try {
        Log.i(
            "isDayChanged",
            "" + calculation.isDayChanged(todayPrayers.sDate)
        )

        if (calculation.isDayChanged(todayPrayers.sDate)) {
            startLoading()
        }

        updatePrayersFlow()
        updateRamadanFlow()
        updateNextPrayerFlow()
        updateDateFlow()
    } catch (e: Exception) {
        Log.i(
            "Flow updates",
            e.message.toString()
        )
        _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_down)) }
    }

    /**update the time to next prayer*/
    private fun updateNextPrayerFlow() {
        _uiNextPrayer.update { calculation.calculateNextPrayerInfo(todayPrayers.toTimePrayer()) }
    }

    /** Build text for dates*/
    private fun updateDateFlow() {
        val calendar = Calendar.getInstance()
        _uiDateInfo.update {
            DateConfig(
                dayName = resourceProvider.getString(getDayName(calendar[Calendar.DAY_OF_WEEK])),
                moonDate = formatter.formatDateText(todayPrayers.mDate, false),
                sunDate = formatter.formatDateText(todayPrayers.sDate, true)
            )
        }
    }


    /**Update ramadan boolean flow*/
    private fun updateRamadanFlow() {
        _uiIsRamadan.update { calculation.isRamadan(todayPrayers.mDate) }
    }

    /**Update prayers flow*/
    private fun updatePrayersFlow() {
        _uiPrayers.update { todayPrayers }
    }

    /**Timer to update the ui*/
    private fun startTicks(delay: Long, initialDelay: Long = 0) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(delay)
        }
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
                .map {prayer->
                    prayer.toPrayer()
                }
                .collect { prayers ->
                    prayers.let { composePrayers ->
                        todayPrayers = composePrayers
                        updateScreenFlows()
                        _uiState.update { UiState.Success }
                    }
                }
        }
    }

    companion object {
        const val STRING_DATE_SEPARATOR = "."
    }
}