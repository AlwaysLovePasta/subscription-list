package com.andrea.subscriptionlist.subscription.domain.model

import java.time.Instant

data class ExchangeRate(
    val rates: Map<Currency, Double>,
    val updatedAt: Instant,
)
