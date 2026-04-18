package com.andrea.subscriptionlist.subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.subscriptionlist.subscription.domain.model.ExchangeRate
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.usecase.CalculateMonthlyAverageUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.DeleteSubscriptionUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.GetExchangeRatesUseCase
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
class SubscriptionViewModel @Inject constructor(
    private val getSubscriptions: GetSubscriptionsUseCase,
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val deleteSubscription: DeleteSubscriptionUseCase,
    private val calculateMonthlyAverage: CalculateMonthlyAverageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Loading)
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SubscriptionNavigationEvent>()
    val navigationEvent: SharedFlow<SubscriptionNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.BY_NEXT_BILLING)
    private val _exchangeRate = MutableStateFlow<ExchangeRate?>(null)
    private val _rateError = MutableStateFlow(false)

    init {
        loadExchangeRate()
        observeSubscriptions()
    }

    fun onEvent(event: SubscriptionUiEvent) = when (event) {
        SubscriptionUiEvent.Refresh -> loadExchangeRate()
        SubscriptionUiEvent.NavigateToAdd -> navigate(SubscriptionNavigationEvent.NavigateToAdd)
        is SubscriptionUiEvent.NavigateToEdit -> navigate(SubscriptionNavigationEvent.NavigateToEdit(event.subscriptionId))
        is SubscriptionUiEvent.DeleteSubscription -> onDeleteSubscription(event.subscriptionId)
        is SubscriptionUiEvent.ChangeSortOrder -> _sortOrder.value = event.order
        SubscriptionUiEvent.DismissError -> _rateError.value = false
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
                SubscriptionUiState.Success(
                    items = items,
                    totalMonthlyTwd = items.sumOf { it.monthlyAmountTwd ?: 0.0 },
                    rateUpdatedAt = rate?.updatedAt,
                    rateError = rateError,
                )
            }
            .catch { _uiState.value = SubscriptionUiState.Error(it.message.orEmpty()) }
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

    private fun navigate(event: SubscriptionNavigationEvent) {
        viewModelScope.launch { _navigationEvent.emit(event) }
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
