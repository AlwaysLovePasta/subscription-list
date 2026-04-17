package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.subscription.domain.model.ExchangeRate
import com.andrea.subscriptionlist.subscription.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository,
) {
    suspend operator fun invoke(): Result<ExchangeRate> = runCatching { repository.getLatest() }
}
