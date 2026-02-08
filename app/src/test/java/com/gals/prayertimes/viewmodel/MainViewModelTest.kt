package com.gals.prayertimes.viewmodel

import app.cash.turbine.test
import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.ads.ConsentManager
import com.gals.prayertimes.main.MainViewModel
import com.gals.prayertimes.main.model.UiState
import com.gals.prayertimes.permissions.manager.PermissionsManager
import com.gals.prayertimes.repository.PrayersRepository
import com.gals.prayertimes.repository.local.entities.PrayerEntity
import com.gals.prayertimes.utils.Formatter
import com.gals.prayertimes.utils.PrayerCalculation
import com.gals.prayertimes.utils.ResourceProvider
import com.gals.prayertimes.utils.TestScreenUpdater
import com.gals.prayertimes.viewmodel.utils.TestDispatcherRule
import com.gals.prayertimes.viewmodel.utils.anyString
import com.gals.prayertimes.viewmodel.utils.dateString
import com.gals.prayertimes.viewmodel.utils.testNextPrayerConfig
import com.gals.prayertimes.viewmodel.utils.testPrayerEntity
import com.gals.prayertimes.viewmodel.utils.testUiPrayer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val rule = TestDispatcherRule()

    private val mockPrayersRepository = mockk<PrayersRepository>()
    private val mockResourceProvider = mockk<ResourceProvider>()
    private val mockFormatter = mockk<Formatter>()
    private val mockCalculation = mockk<PrayerCalculation>()
    private val mockScreenUpdater = mockk<TestScreenUpdater>()
    private val mockPermissionsManager = mockk<PermissionsManager>()
    private val mockConsentManager = mockk<ConsentManager>()
    private val mockAdsManager = mockk<AdsManager>()


    @Before
    fun setup() {
        setNetworkRequest(testPrayerEntity) {}
        setResourceProviderMessage(anyString)

        coEvery {
            mockScreenUpdater.startTicks(any())
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
        every { mockConsentManager.canRequestAds } returns false
        every { mockConsentManager.isPrivacyOptionsRequired } returns false
        every { mockAdsManager.initAdsSDK() } just runs
    }

    @Test
    fun `Given consent is not required, when view model created, then show loading`() =
        runTest {
            every { mockConsentManager.canRequestAds } returns true
            val viewModel = createViewModel()
            viewModel.uiState.test {
                assertEquals(UiState.Loading, awaitItem())
            }
        }

    @Test
    fun `Given consent is required, when view model created, then show consent`() =
        runTest {
            every { mockConsentManager.canRequestAds } returns false
            val viewModel = createViewModel()
            viewModel.uiState.test {
                assertEquals(UiState.Consent, awaitItem())
            }
        }

    @Test
    fun `Given valid request from be, when view model start loading, then show success result`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.startLoading()
            viewModel.uiState.test {
                assertEquals(UiState.Success(testUiPrayer, false), awaitItem())
            }
        }

    @Test
    fun `Given request is ongoing, when start loading, then skip loading to success state`() =
        runTest {
            setNetworkRequest(prayerEntity = testPrayerEntity) {
                delay(5)
            }

            val viewModel = createViewModel()
            viewModel.startLoading()
            viewModel.uiState.test {
                assertEquals(UiState.Success(testUiPrayer, false), awaitItem())
            }
        }

    //TODO: test error cases
    //TODO: test retry method

    private fun setNetworkRequest(prayerEntity: PrayerEntity, block: suspend () -> Unit) {
        coEvery {
            mockPrayersRepository.fetchPrayer(any())
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
        dispatcher = Dispatchers.Main,
        screenUpdater = mockScreenUpdater,
        prayersRepository = mockPrayersRepository,
        resourceProvider = mockResourceProvider,
        formatter = mockFormatter,
        calculation = mockCalculation,
        permissionsManager = mockPermissionsManager,
        consentManager = mockConsentManager,
        adsManager = mockAdsManager
    )
}