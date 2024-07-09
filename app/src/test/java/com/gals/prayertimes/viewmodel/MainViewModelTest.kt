package com.gals.prayertimes.viewmodel

import app.cash.turbine.test
import com.gals.prayertimes.model.ConnectivityException
import com.gals.prayertimes.model.NetworkException
import com.gals.prayertimes.model.UiState
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.TestScreenUpdater
import com.gals.prayertimes.utils.toPrayer
import com.gals.prayertimes.viewmodel.utils.TestDispatcherRule
import com.gals.prayertimes.viewmodel.utils.anyString
import com.gals.prayertimes.viewmodel.utils.dateString
import com.gals.prayertimes.viewmodel.utils.errorMessage
import com.gals.prayertimes.viewmodel.utils.testNextPrayerConfig
import com.gals.prayertimes.viewmodel.utils.testPrayer
import com.gals.prayertimes.viewmodel.utils.testPrayerEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class MainViewModelTest {

    @get:Rule
    val rule = TestDispatcherRule()

    private val mockRepository = mockk<Repository>()
    private val mockResourceProvider = mockk<ResourceProvider>()
    private val mockFormatter = mockk<Formatter>()
    private val mockCalculation = mockk<PrayerCalculation>()
    private val mockScreenUpdater = mockk<TestScreenUpdater>()

    @Before
    fun setup() {
        setNetworkRequest(testPrayerEntity) {}
        setResourceProviderMessage(anyString)

        coEvery {
            mockScreenUpdater.startTicks(any(), any())
        } returns flowOf(Unit)

        every {
            mockFormatter.formatDateText(any(), any())
        } returns dateString

        every { mockCalculation.isDayChanged(any()) } returns false

        every {
            mockCalculation.calculateNextPrayerInfo(
                any(),
                any()
            )
        } returns testNextPrayerConfig
    }

    @Test
    fun `Given valid request from be, when view model start loading, then show success result`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.uiState.test {
                assertEquals(UiState.Success(testPrayer), awaitItem())
            }
        }

    @Test
    fun `Given request failed, when start loading, then error state with error message`() =
        runTest {
            setNetworkRequest(prayerEntity = testPrayerEntity) {
                delay(2)
            }

            val viewModel = createViewModel()

            viewModel.uiState.test {
                assertEquals(UiState.Error(anyString), awaitItem())
            }
        }

    //TODO: test error cases
    //TODO: test retry method

    private fun setNetworkRequest(prayerEntity: PrayerEntity, block: suspend () -> Unit) {
        coEvery {
            mockRepository.fetchComposePrayer(any())
        } coAnswers {
            block()
            flowOf(prayerEntity)
        }
    }

    private fun setResourceProviderMessage(message: String) {
        every {
            mockResourceProvider.getString(any())
        } returns message
    }

    private fun createViewModel() = MainViewModel(
        repository = mockRepository,
        resourceProvider = mockResourceProvider,
        formatter = mockFormatter,
        calculation = mockCalculation,
        screenUpdater = mockScreenUpdater
    )
}