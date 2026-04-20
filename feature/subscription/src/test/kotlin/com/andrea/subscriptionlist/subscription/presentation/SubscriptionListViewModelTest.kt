package com.andrea.subscriptionlist.subscription.presentation

import app.cash.turbine.test
import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import com.andrea.subscriptionlist.exchangerate.domain.usecase.GetExchangeRatesUseCase
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.usecase.CalculateMonthlyAverageUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.DeleteSubscriptionUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.GetSubscriptionsUseCase
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SortOrder
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListNavigationEvent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiEvent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiState
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getSubscriptions: GetSubscriptionsUseCase = mockk()
    private val getExchangeRates: GetExchangeRatesUseCase = mockk()
    private val deleteSubscription: DeleteSubscriptionUseCase = mockk()
    private val calculateMonthlyAverage = CalculateMonthlyAverageUseCase()

    private val subscriptionsFlow = MutableStateFlow<List<Subscription>>(emptyList())

    private val sampleRate = ExchangeRate(
        rates = mapOf(Currency.USD to 32.5, Currency.EUR to 35.0),
        updatedAt = Instant.EPOCH,
    )

    private val netflix = Subscription(
        id = "1",
        serviceName = "Netflix",
        planName = "Premium 4K",
        price = 22.99,
        currency = Currency.USD,
        billingCycleMonths = 1,
        nextBillingDate = LocalDate.of(2026, 5, 1),
    )

    private val spotify = Subscription(
        id = "2",
        serviceName = "Spotify",
        planName = "Individual",
        price = 149.0,
        currency = Currency.TWD,
        billingCycleMonths = 1,
        nextBillingDate = LocalDate.of(2026, 4, 24),
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSubscriptions() } returns subscriptionsFlow
        coEvery { getExchangeRates() } returns Result.success(sampleRate)
        coEvery { deleteSubscription(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = SubscriptionListViewModel(
        getSubscriptions = getSubscriptions,
        getExchangeRates = getExchangeRates,
        deleteSubscription = deleteSubscription,
        calculateMonthlyAverage = calculateMonthlyAverage,
    )

    @Test
    fun `success state contains items with calculated monthly amount twd`() = runTest {
        subscriptionsFlow.value = listOf(netflix)
        val vm = createViewModel()

        val state = vm.uiState.value as SubscriptionListUiState.Success
        assertEquals(1, state.items.size)
        assertEquals(22.99 * 32.5, state.items.first().monthlyAmountTwd!!, 0.01)
    }

    @Test
    fun `monthly amount is null when exchange rate fails`() = runTest {
        coEvery { getExchangeRates() } returns Result.failure(IOException("network error"))
        subscriptionsFlow.value = listOf(netflix)
        val vm = createViewModel()

        val state = vm.uiState.value as SubscriptionListUiState.Success
        assertNull(state.items.first().monthlyAmountTwd)
    }

    @Test
    fun `rate error is true when exchange rate fails`() = runTest {
        coEvery { getExchangeRates() } returns Result.failure(IOException("network error"))
        val vm = createViewModel()

        val state = vm.uiState.value as SubscriptionListUiState.Success
        assertTrue(state.rateError)
    }

    @Test
    fun `dismiss error clears rate error`() = runTest {
        coEvery { getExchangeRates() } returns Result.failure(IOException("network error"))
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.DismissError)

        assertFalse((vm.uiState.value as SubscriptionListUiState.Success).rateError)
    }

    @Test
    fun `change sort order to BY_NAME sorts items alphabetically`() = runTest {
        subscriptionsFlow.value = listOf(spotify, netflix)
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.ChangeSortOrder(SortOrder.BY_NAME))

        val names = (vm.uiState.value as SubscriptionListUiState.Success).items.map { it.serviceName }
        assertEquals(listOf("Netflix", "Spotify"), names)
    }

    @Test
    fun `change sort order to BY_MONTHLY_COST sorts by cost descending`() = runTest {
        subscriptionsFlow.value = listOf(spotify, netflix)
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.ChangeSortOrder(SortOrder.BY_MONTHLY_COST))

        val first = (vm.uiState.value as SubscriptionListUiState.Success).items.first()
        assertEquals("Netflix", first.serviceName)
    }

    @Test
    fun `change sort order to BY_NEXT_BILLING sorts by date ascending`() = runTest {
        subscriptionsFlow.value = listOf(netflix, spotify)
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.ChangeSortOrder(SortOrder.BY_NEXT_BILLING))

        val first = (vm.uiState.value as SubscriptionListUiState.Success).items.first()
        assertEquals("Spotify", first.serviceName)
    }

    @Test
    fun `delete subscription invokes use case with correct id`() = runTest {
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.DeleteSubscription("1"))

        coVerify { deleteSubscription("1") }
    }

    @Test
    fun `navigate to add emits NavigateToAdd event`() = runTest {
        val vm = createViewModel()

        vm.navigationEvent.test {
            vm.onEvent(SubscriptionListUiEvent.NavigateToAdd)
            assertEquals(SubscriptionListNavigationEvent.NavigateToAdd, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigate to edit emits NavigateToEdit with correct id`() = runTest {
        val vm = createViewModel()

        vm.navigationEvent.test {
            vm.onEvent(SubscriptionListUiEvent.NavigateToEdit("1"))
            assertEquals(SubscriptionListNavigationEvent.NavigateToEdit("1"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh reloads exchange rate`() = runTest {
        val vm = createViewModel()

        vm.onEvent(SubscriptionListUiEvent.Refresh)

        coVerify(exactly = 2) { getExchangeRates() }
    }

    @Test
    fun `db error transitions to Error state`() = runTest {
        every { getSubscriptions() } returns flow { throw RuntimeException("db error") }
        val vm = createViewModel()

        assertInstanceOf(SubscriptionListUiState.Error::class.java, vm.uiState.value)
    }

    @Test
    fun `total monthly twd is sum of all items monthly amounts`() = runTest {
        subscriptionsFlow.value = listOf(netflix, spotify)
        val vm = createViewModel()

        val state = vm.uiState.value as SubscriptionListUiState.Success
        val expected = (22.99 * 32.5) + 149.0
        assertEquals(expected, state.totalMonthlyTwd, 0.01)
    }
}
