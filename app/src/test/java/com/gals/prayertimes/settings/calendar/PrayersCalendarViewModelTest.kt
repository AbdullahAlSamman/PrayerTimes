package com.gals.prayertimes.settings.calendar

import app.cash.turbine.test
import com.gals.prayertimes.R
import com.gals.prayertimes.common.ConnectivityException
import com.gals.prayertimes.common.ServerException
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.settings.calendar.model.PrayersCalendarUiState
import com.gals.prayertimes.settings.calendar.tracker.PrayerCalendarTracker
import com.gals.prayertimes.utils.DateFormatter
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.viewmodel.utils.MainDispatcherExtension
import com.gals.prayertimes.viewmodel.utils.anyString
import com.gals.prayertimes.viewmodel.utils.dateString
import com.gals.prayertimes.viewmodel.utils.testPrayerEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class PrayersCalendarViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private val mockRepository = mockk<PrayersRepository>()
    private val mockDateFormatter = mockk<DateFormatter>()
    private val mockResourceProvider = mockk<ResourceProvider>()
    private val mockTracker = mockk<PrayerCalendarTracker>()

    private lateinit var viewModel: PrayersCalendarViewModel

    @BeforeEach
    fun setup() {
        viewModel = PrayersCalendarViewModel(
            repository = mockRepository,
            dateFormatter = mockDateFormatter,
            resourceProvider = mockResourceProvider,
            tracker = mockTracker
        )

        every { mockTracker.selectedDate(any()) } just runs
        every { mockDateFormatter.formatMillisToDateString(any()) } returns dateString
        every { mockDateFormatter.formatMoonDateText(any()) } returns dateString
        every { mockDateFormatter.formatSunDateText(any()) } returns dateString
        every { mockResourceProvider.getString(any()) } returns anyString
    }

    @Test
    fun `Given successful response, when fetching prayers for date, then emit Success state`() = runTest {
        coEvery { mockRepository.getRemotePrayer(any()) } returns flowOf(testPrayerEntity)

        viewModel.uiState.test {
            viewModel.fetchPrayersForDate(123456789L)
            assertEquals(PrayersCalendarUiState.Loading, awaitItem())
            val successState = awaitItem() as PrayersCalendarUiState.Success
            assertEquals(testPrayerEntity.fajer, successState.data.prayers[0].time)
            cancelAndIgnoreRemainingEvents()
        }

        verify { mockTracker.selectedDate(dateString) }
    }

    @Test
    fun `Given connectivity error, when fetching prayers for date, then emit Error state with internet check message`() = runTest {
        val errorMessage = "Check internet"
        every { mockResourceProvider.getString(R.string.text_error_check_internet) } returns errorMessage
        coEvery { mockRepository.getRemotePrayer(any()) } returns flow {
            throw ConnectivityException("No internet")
        }

        viewModel.uiState.test {
            viewModel.fetchPrayersForDate(123456789L)
            assertEquals(PrayersCalendarUiState.Loading, awaitItem())
            assertEquals(PrayersCalendarUiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given server error, when fetching prayers for date, then emit Error state with no data message`() = runTest {
        val errorMessage = "Server no data"
        every { mockResourceProvider.getString(R.string.text_error_server_no_data) } returns errorMessage
        coEvery { mockRepository.getRemotePrayer(any()) } returns flow {
            throw ServerException("No data")
        }

        viewModel.uiState.test {
            viewModel.fetchPrayersForDate(123456789L)
            assertEquals(PrayersCalendarUiState.Loading, awaitItem())
            assertEquals(PrayersCalendarUiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given generic error, when fetching prayers for date, then emit Error state with server down message`() = runTest {
        val errorMessage = "Server down"
        every { mockResourceProvider.getString(R.string.text_error_server_down) } returns errorMessage
        coEvery { mockRepository.getRemotePrayer(any()) } returns flow {
            throw Exception()
        }

        viewModel.uiState.test {
            viewModel.fetchPrayersForDate(123456789L)
            assertEquals(PrayersCalendarUiState.Loading, awaitItem())
            assertEquals(PrayersCalendarUiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
