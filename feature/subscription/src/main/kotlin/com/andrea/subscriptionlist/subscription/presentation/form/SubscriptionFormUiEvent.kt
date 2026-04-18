package com.andrea.subscriptionlist.subscription.presentation.form

import com.andrea.subscriptionlist.core.common.Currency
import java.time.LocalDate

sealed interface SubscriptionFormUiEvent {
    data class ServiceNameChanged(val value: String) : SubscriptionFormUiEvent
    data class PlanNameChanged(val value: String) : SubscriptionFormUiEvent
    data class PriceChanged(val value: String) : SubscriptionFormUiEvent
    data class CurrencyChanged(val currency: Currency) : SubscriptionFormUiEvent
    data class BillingCycleChanged(val months: Int) : SubscriptionFormUiEvent
    data class NextBillingDateChanged(val date: LocalDate) : SubscriptionFormUiEvent
    data object Save : SubscriptionFormUiEvent
    data object Delete : SubscriptionFormUiEvent
}
