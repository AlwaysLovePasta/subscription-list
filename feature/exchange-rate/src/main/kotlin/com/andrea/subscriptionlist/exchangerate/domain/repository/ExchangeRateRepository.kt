package com.andrea.subscriptionlist.exchangerate.domain.repository

import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate

interface ExchangeRateRepository {
    suspend fun getLatest(): ExchangeRate
}
