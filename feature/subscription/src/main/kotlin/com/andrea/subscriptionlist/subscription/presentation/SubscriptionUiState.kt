package com.andrea.subscriptionlist.subscription.presentation

import com.andrea.subscriptionlist.subscription.domain.model.Currency
import java.time.Instant
import java.time.LocalDate

sealed interface SubscriptionUiState {
    data object Loading : SubscriptionUiState
    data class Success(
        val items: List<SubscriptionItemUiModel>,
        val totalMonthlyTwd: Double,
        val rateUpdatedAt: Instant?,
        val rateError: Boolean = false,
    ) : SubscriptionUiState
    data class Error(val message: String) : SubscriptionUiState
}

data class SubscriptionItemUiModel(
    val id: String,
    val serviceName: String,
    val planName: String,
    val price: Double,
    val currency: Currency,
    val billingCycleMonths: Int,
    val nextBillingDate: LocalDate,
    val monthlyAmountTwd: Double?,
)
