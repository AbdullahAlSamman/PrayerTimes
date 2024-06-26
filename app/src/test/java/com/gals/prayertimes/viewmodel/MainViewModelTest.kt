package com.gals.prayertimes.viewmodel

import app.cash.turbine.test
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.viewmodel.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepository = mockk<Repository>()
    private val mockResourceProvider = mockk<ResourceProvider>()
    private val mockFormatter = mockk<Formatter>()
    private val mockCalculation = mockk<PrayerCalculation>()

    @Test
    fun `Given valid request from be, when view model start loading, then show success result`() =
        runTest(StandardTestDispatcher()) {
            coEvery {
                mockRepository.fetchComposePrayer(any())
            } returns flowOf(PrayerEntity())

            coEvery {
                mockResourceProvider.getString(any())
            } returns ""

            coEvery {
                mockFormatter.formatDateText(any(), any())
            } returns "date_string"

            coEvery { mockCalculation.isDayChanged(any()) } returns false

            coEvery {
                mockCalculation.calculateNextPrayerInfo(
                    any(),
                    any()
                )
            } returns NextPrayerConfig()

            val viewModel = createViewModel()

            viewModel.uiState.test {
                assertEquals(awaitItem(), UiState.Success)
            }
        }

    private fun createViewModel() = MainViewModel(
        repository = mockRepository,
        resourceProvider = mockResourceProvider,
        formatter = mockFormatter,
        calculation = mockCalculation
    )
}