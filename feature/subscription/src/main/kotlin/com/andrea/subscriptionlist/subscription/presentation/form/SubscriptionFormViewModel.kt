package com.andrea.subscriptionlist.subscription.presentation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import com.andrea.subscriptionlist.exchangerate.domain.usecase.GetExchangeRatesUseCase
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.usecase.AddSubscriptionUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.CalculateMonthlyAverageUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.DeleteSubscriptionUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.GetSubscriptionsUseCase
import com.andrea.subscriptionlist.subscription.domain.usecase.UpdateSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SubscriptionFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSubscriptions: GetSubscriptionsUseCase,
    private val addSubscription: AddSubscriptionUseCase,
    private val updateSubscription: UpdateSubscriptionUseCase,
    private val deleteSubscription: DeleteSubscriptionUseCase,
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val calculateMonthlyAverage: CalculateMonthlyAverageUseCase,
) : ViewModel() {

    private val subscriptionId: String? = savedStateHandle["subscriptionId"]

    private val _uiState = MutableStateFlow(
        SubscriptionFormUiState(mode = if (subscriptionId == null) FormMode.ADD else FormMode.EDIT)
    )
    val uiState: StateFlow<SubscriptionFormUiState> = _uiState.asStateFlow()

    private var exchangeRate: ExchangeRate? = null

    init {
        loadExchangeRate()
        if (subscriptionId != null) loadSubscription(subscriptionId)
    }

    fun onEvent(event: SubscriptionFormUiEvent) = when (event) {
        is SubscriptionFormUiEvent.ServiceNameChanged -> updateField { copy(serviceName = event.value) }
        is SubscriptionFormUiEvent.PlanNameChanged -> updateField { copy(planName = event.value) }
        is SubscriptionFormUiEvent.PriceChanged -> updateField { copy(price = event.value) }
        is SubscriptionFormUiEvent.CurrencyChanged -> updateField { copy(currency = event.currency) }
        is SubscriptionFormUiEvent.BillingCycleChanged -> updateField { copy(billingCycleMonths = event.months) }
        is SubscriptionFormUiEvent.NextBillingDateChanged -> updateField { copy(nextBillingDate = event.date) }
        SubscriptionFormUiEvent.Save -> save()
        SubscriptionFormUiEvent.Delete -> _uiState.update { it.copy(showDeleteConfirmation = true) }
        SubscriptionFormUiEvent.ConfirmDelete -> delete()
        SubscriptionFormUiEvent.DismissDeleteConfirmation -> _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    private fun loadExchangeRate() {
        viewModelScope.launch {
            getExchangeRates().onSuccess { rate ->
                exchangeRate = rate
                recalculate()
            }
        }
    }

    private fun loadSubscription(id: String) {
        viewModelScope.launch {
            val sub = getSubscriptions().first().find { it.id == id } ?: return@launch
            _uiState.update {
                it.copy(
                    serviceName = sub.serviceName,
                    planName = sub.planName,
                    price = sub.price.toBigDecimal().stripTrailingZeros().toPlainString(),
                    currency = sub.currency,
                    billingCycleMonths = sub.billingCycleMonths,
                    nextBillingDate = sub.nextBillingDate,
                )
            }
            recalculate()
        }
    }

    private fun updateField(transform: SubscriptionFormUiState.() -> SubscriptionFormUiState) {
        _uiState.update(transform)
        recalculate()
    }

    private fun recalculate() {
        _uiState.update { state ->
            val price = state.price.toDoubleOrNull() ?: 0.0
            val rateToTwd = rateToTwd(state.currency)
            val monthly = if (rateToTwd != null && price > 0 && state.billingCycleMonths >= 1) {
                price * rateToTwd / state.billingCycleMonths
            } else null
            state.copy(
                monthlyAmountTwd = monthly,
                exchangeRateToTwd = rateToTwd,
                isFormValid = state.serviceName.isNotBlank()
                    && state.planName.isNotBlank()
                    && price > 0
                    && state.billingCycleMonths >= 1,
            )
        }
    }

    private fun rateToTwd(currency: Currency): Double? = when (currency) {
        Currency.TWD -> 1.0
        else -> exchangeRate?.rates?.get(currency)
    }

    private fun save() {
        val state = _uiState.value
        if (!state.isFormValid) return
        val subscription = Subscription(
            id = subscriptionId ?: UUID.randomUUID().toString(),
            serviceName = state.serviceName.trim(),
            planName = state.planName.trim(),
            price = state.price.toDouble(),
            currency = state.currency,
            billingCycleMonths = state.billingCycleMonths,
            nextBillingDate = state.nextBillingDate,
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (state.mode == FormMode.ADD) addSubscription(subscription)
            else updateSubscription(subscription)
            _uiState.update { it.copy(isSaving = false, isDone = true) }
        }
    }

    private fun delete() {
        val id = subscriptionId ?: return
        viewModelScope.launch {
            deleteSubscription(id)
            _uiState.update { it.copy(isDone = true) }
        }
    }
}
