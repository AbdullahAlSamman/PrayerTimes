package com.gals.prayertimes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.R
import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.DefaultDispatcher
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.common.ViewModelScreenUpdater
import com.gals.prayertimes.common.mappers.toPrayer
import com.gals.prayertimes.common.mappers.toTimePrayer
import com.gals.prayertimes.common.mappers.toUiNextPrayer
import com.gals.prayertimes.common.mappers.todayDate
import com.gals.prayertimes.main.model.UiNextPrayer
import com.gals.prayertimes.main.model.UiState
import com.gals.prayertimes.permissions.manager.PermissionsManager
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.ScreenUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ViewModelScreenUpdater private val screenUpdater: ScreenUpdater,
    private val prayersRepository: PrayersRepository,
    private val resourceProvider: ResourceProvider,
    private val calculation: PrayerCalculation,
    private val formatter: Formatter,
    private val permissionsManager: PermissionsManager
) : ViewModel() {
    private var todayPrayers = PrayerEntity()
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _uiNextPrayer = MutableStateFlow(UiNextPrayer())

    val uiState: StateFlow<UiState> = _uiState
    val nextPrayer: StateFlow<UiNextPrayer> = _uiNextPrayer.asStateFlow()

    init {
        startLoading(dispatcher = dispatcher)
    }

    fun startUiTicks() {
        screenUpdater.startTicks(delay = TICKS_DELAY)
            .onEach {
                updateScreenStates()
            }.launchIn(viewModelScope)
    }

    /**retry method to call from ui*/
    fun reload() {
        startLoading(showLoadingScreen = true, dispatcher = dispatcher)
    }

    fun areAllPermissionsGranted(): Boolean = permissionsManager.areAllPermissionsGranted()

    /**update all flows related to ui*/
    private fun updateScreenStates() = try {
        Timber.i("is day changed: ${calculation.isDayChanged(todayPrayers.sDate)}")

        if (calculation.isDayChanged(todayPrayers.sDate)) {
            startLoading(dispatcher = dispatcher)
        }

        updateNextPrayerState()

    } catch (error: Exception) {
        Timber.e("flow update error: ${error.message.toString()}")
        error.toUiError()
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
    private fun startLoading(showLoadingScreen: Boolean = false, dispatcher: CoroutineDispatcher) {
        if (showLoadingScreen) {
            _uiState.update { UiState.Loading }
        }
        viewModelScope.launch(context = dispatcher) {
            prayersRepository.fetchPrayer(todayDate())
                .catch { cause -> cause.toUiError() }
                .map { prayer ->
                    todayPrayers = prayer
                    prayer.toPrayer(resourceProvider, formatter)
                }.collect { prayers ->
                    prayers.let { composePrayers ->
                        updateScreenStates()
                        _uiState.update { UiState.Success(uiPrayer = composePrayers) }
                    }
                }
        }
    }

    private fun Throwable.toUiError() =
        when (this) {
            is ConnectivityException -> _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_check_internet)) }
            is ServerException -> _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_no_data)) }
            else -> _uiState.update { UiState.Error(resourceProvider.getString(R.string.text_error_server_down)) }
        }

    companion object {
        const val STRING_DATE_SEPARATOR = "."
        const val TICKS_DELAY: Long = 25_000
    }
}