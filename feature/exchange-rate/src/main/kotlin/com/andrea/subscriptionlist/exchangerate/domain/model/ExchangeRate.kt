package com.andrea.subscriptionlist.exchangerate.domain.model

import com.andrea.subscriptionlist.core.common.Currency
import java.time.Instant

data class ExchangeRate(
    val rates: Map<Currency, Double>,
    val updatedAt: Instant,
)
