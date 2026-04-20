package com.andrea.subscriptionlist.subscription.presentation.stateholders

import com.andrea.subscriptionlist.core.common.Currency
import java.time.LocalDate

data class SubscriptionFormUiState(
    val mode: FormMode,
    val serviceName: String = "",
    val planName: String = "",
    val price: String = "",
    val currency: Currency = Currency.USD,
    val billingCycleMonths: Int = 1,
    val nextBillingDate: LocalDate = LocalDate.now().plusMonths(1),
    val monthlyAmountTwd: Double? = null,
    val exchangeRateToTwd: Double? = null,
    val isFormValid: Boolean = false,
    val isSaving: Boolean = false,
    val isDone: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
)

enum class FormMode { ADD, EDIT }
