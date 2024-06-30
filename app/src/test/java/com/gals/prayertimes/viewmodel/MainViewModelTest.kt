package com.gals.prayertimes.viewmodel

import app.cash.turbine.test
import com.gals.prayertimes.model.NextPrayerConfig
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.viewmodel.utils.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val rule = TestDispatcherRule()

    private val mockRepository = mockk<Repository>()
    private val mockResourceProvider = mockk<ResourceProvider>()
    private val mockFormatter = mockk<Formatter>()
    private val mockCalculation = mockk<PrayerCalculation>()

    @Test
    fun `Given valid request from be, when view model start loading, then show success result`() =
        runTest {
            coEvery {
                mockRepository.fetchComposePrayer(any())
            } coAnswers {
                flowOf(
                    PrayerEntity(
                        objectId = "id",
                        sDate = "01.02.2023",
                        mDate = "01.03.1443",
                        fajer = "04:00",
                        sunrise = "06:00",
                        duhr = "13:00",
                        asr = "16:00",
                        maghrib = "20:00",
                        isha = "22:00"
                    )
                )
            }

            every {
                mockResourceProvider.getString(any())
            } returns "any_string"

            every {
                mockFormatter.formatDateText(any(), any())
            } returns "date_string"

            every { mockCalculation.isDayChanged(any()) } returns false

            every {
                mockCalculation.calculateNextPrayerInfo(
                    any(),
                    any()
                )
            } returns NextPrayerConfig(
                nextPrayerBanner = "asr",
                nextPrayerName = "Asr",
                nextPrayerTime = "16:00"
            )

            val viewModel = createViewModel()

            viewModel.uiState.test {
                assertEquals(UiState.Success, awaitItem())
            }
        }

    private fun createViewModel() = MainViewModel(
        repository = mockRepository,
        resourceProvider = mockResourceProvider,
        formatter = mockFormatter,
        calculation = mockCalculation
    )
}