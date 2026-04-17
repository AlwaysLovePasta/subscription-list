package com.andrea.subscriptionlist.subscription.domain.repository

import com.andrea.subscriptionlist.subscription.domain.model.ExchangeRate

interface ExchangeRateRepository {
    suspend fun getLatest(): ExchangeRate
}
