package com.andrea.subscriptionlist.subscription.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import com.andrea.subscriptionlist.exchangerate.domain.usecase.GetExchangeRatesUseCase
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.usecase.CalculateMonthlyAverageUseCase
import java.text.NumberFormat
import com.andrea.subscriptionlist.subscription.domain.usecase.DeleteSubscriptionUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.GetSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionListViewModel @Inject constructor(
    private val getSubscriptions: GetSubscriptionsUseCase,
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val deleteSubscription: DeleteSubscriptionUseCase,
    private val calculateMonthlyAverage: CalculateMonthlyAverageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubscriptionListUiState>(SubscriptionListUiState.Loading)
    val uiState: StateFlow<SubscriptionListUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SubscriptionListNavigationEvent>()
    val navigationEvent: SharedFlow<SubscriptionListNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.BY_NEXT_BILLING)
    private val _exchangeRate = MutableStateFlow<ExchangeRate?>(null)
    private val _rateError = MutableStateFlow(false)

    init {
        loadExchangeRate()
        observeSubscriptions()
    }

    fun onEvent(event: SubscriptionListUiEvent) = when (event) {
        SubscriptionListUiEvent.Refresh -> loadExchangeRate()
        SubscriptionListUiEvent.NavigateToAdd -> navigate(SubscriptionListNavigationEvent.NavigateToAdd)
        is SubscriptionListUiEvent.NavigateToEdit -> navigate(SubscriptionListNavigationEvent.NavigateToEdit(event.subscriptionId))
        is SubscriptionListUiEvent.DeleteSubscription -> onDeleteSubscription(event.subscriptionId)
        is SubscriptionListUiEvent.ChangeSortOrder -> _sortOrder.value = event.order
        SubscriptionListUiEvent.DismissError -> _rateError.value = false
    }

    private fun observeSubscriptions() {
        viewModelScope.launch {
            combine(
                getSubscriptions(),
                _exchangeRate,
                _sortOrder,
                _rateError,
            ) { subscriptions, rate, sortOrder, rateError ->
                val items = subscriptions
                    .map { it.toUiModel(rate) }
                    .sortedWith(sortOrder)
                SubscriptionListUiState.Success(
                    items = items,
                    totalMonthlyTwd = items.sumOf { it.monthlyAmountTwd ?: 0.0 },
                    fxSummary = buildFxSummary(subscriptions, rate),
                    sortOrder = sortOrder,
                    rateUpdatedAt = rate?.updatedAt,
                    rateError = rateError,
                )
            }
            .catch { _uiState.value = SubscriptionListUiState.Error(it.message.orEmpty()) }
            .collect { _uiState.value = it }
        }
    }

    private fun loadExchangeRate() {
        _rateError.value = false
        viewModelScope.launch {
            getExchangeRates()
                .onSuccess { _exchangeRate.value = it }
                .onFailure { _rateError.value = true }
        }
    }

    private fun onDeleteSubscription(id: String) {
        viewModelScope.launch { deleteSubscription(id) }
    }

    private fun navigate(event: SubscriptionListNavigationEvent) {
        viewModelScope.launch { _navigationEvent.emit(event) }
    }

    private fun buildFxSummary(subscriptions: List<Subscription>, rate: ExchangeRate?): String? {
        if (rate == null) return null
        val nf = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 1 }
        return subscriptions
            .map { it.currency }
            .filter { it != Currency.TWD }
            .distinct()
            .sorted()
            .mapNotNull { currency -> rate.rates[currency]?.let { "${currency.name} ${nf.format(it)}" } }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" · ")
    }

    private fun Subscription.toUiModel(rate: ExchangeRate?) = SubscriptionItemUiModel(
        id = id,
        serviceName = serviceName,
        planName = planName,
        price = price,
        currency = currency,
        billingCycleMonths = billingCycleMonths,
        nextBillingDate = nextBillingDate,
        monthlyAmountTwd = rate?.let { calculateMonthlyAverage(this, it) },
    )

    private fun List<SubscriptionItemUiModel>.sortedWith(order: SortOrder) = when (order) {
        SortOrder.BY_NAME -> sortedBy { it.serviceName }
        SortOrder.BY_MONTHLY_COST -> sortedByDescending { it.monthlyAmountTwd ?: -1.0 }
        SortOrder.BY_NEXT_BILLING -> sortedBy { it.nextBillingDate }
    }
}
