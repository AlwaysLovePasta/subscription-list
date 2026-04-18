package com.andrea.subscriptionlist.exchangerate.domain.usecase

import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import com.andrea.subscriptionlist.exchangerate.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRateRepository,
) {
    suspend operator fun invoke(): Result<ExchangeRate> = runCatching { repository.getLatest() }
}
