package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.subscription.domain.model.Currency
import com.andrea.subscriptionlist.subscription.domain.model.ExchangeRate
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import javax.inject.Inject

class CalculateMonthlyAverageUseCase @Inject constructor() {
    operator fun invoke(subscription: Subscription, rate: ExchangeRate): Double? {
        val rateToTwd = when(subscription.currency) {
            Currency.TWD -> 1.0
            else -> rate.rates[subscription.currency] ?: return null
        }
        return subscription.price * rateToTwd / subscription.billingCycleMonths
    }
}
