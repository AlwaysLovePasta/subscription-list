package com.andrea.subscriptionlist.subscription.presentation.list

import com.andrea.subscriptionlist.core.common.Currency
import java.time.Instant
import java.time.LocalDate

sealed interface SubscriptionListUiState {
    data object Loading : SubscriptionListUiState
    data class Success(
        val items: List<SubscriptionItemUiModel>,
        val totalMonthlyTwd: Double,
        val sortOrder: SortOrder,
        val rateUpdatedAt: Instant?,
        val rateError: Boolean = false,
    ) : SubscriptionListUiState
    data class Error(val message: String) : SubscriptionListUiState
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
