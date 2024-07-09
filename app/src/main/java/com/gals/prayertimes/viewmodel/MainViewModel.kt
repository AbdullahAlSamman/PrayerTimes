package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.NetworkException
import com.gals.prayertimes.model.UiNextPrayer
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.model.ViewModelScreenUpdater
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.ScreenUpdater
import com.gals.prayertimes.utils.getTodayDate
import com.gals.prayertimes.utils.toPrayer
import com.gals.prayertimes.utils.toTimePrayer
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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ViewModelScreenUpdater private val screenUpdater: ScreenUpdater,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val calculation: PrayerCalculation,
    private val formatter: Formatter
) : ViewModel() {
    private var todayPrayers = PrayerEntity()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _uiNextPrayer = MutableStateFlow(UiNextPrayer())

    val uiState: StateFlow<UiState> = _uiState
    val nextPrayer: StateFlow<UiNextPrayer> = _uiNextPrayer.asStateFlow()

    init {
        startLoading()
        //TODO: Ticks starts on screen loaded successfully using launched effect
        screenUpdater.startTicks(delay = TICKS_DELAY, initialDelay = TICKS_INITIAL_DELAY)
            .onEach {
                updateScreenStates()
            }.launchIn(viewModelScope)
    }

    /**retry method to call from ui*/
    fun reload() {
        startLoading(showLoadingScreen = true)
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

        updateNextPrayerState()

    } catch (e: Exception) {
        Log.i(
            "Flow updates",
            "error: ${e.message.toString()}"
        )
        //TODO: not handled correctly show should check for exception type
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

    /**Method to initial loading flow*/
    private fun startLoading(showLoadingScreen: Boolean = false) {
        if (showLoadingScreen) {
            _uiState.update { UiState.Loading }
        }
        viewModelScope.launch {
            repository.fetchComposePrayer(getTodayDate())
                .catch { cause ->
                    when (cause) {
                        is ConnectivityException -> {
                            _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_check_internet)) }
                        }

                        is NetworkException -> {
                            _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_down)) }
                        }
                    }
                }
                .map { prayer ->
                    todayPrayers = prayer
                    prayer.toPrayer(resourceProvider, formatter)
                }
                .collect { prayers ->
                    prayers.let { composePrayers ->
                        updateScreenStates()
                        _uiState.update { UiState.Success(prayer = composePrayers) }
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