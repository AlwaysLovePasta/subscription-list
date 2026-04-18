package com.andrea.subscriptionlist.exchangerate.data.mapper

import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.data.local.ExchangeRateCacheEntity
import com.andrea.subscriptionlist.exchangerate.data.remote.ExchangeRateDto
import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import java.time.Instant

fun ExchangeRateDto.toEntities(updatedAt: Long): List<ExchangeRateCacheEntity> =
    Currency.entries
        .filter { it != Currency.TWD }
        .mapNotNull { currency ->
            val apiRate = conversionRates[currency.name]?.takeIf { it > 0 } ?: return@mapNotNull null
            ExchangeRateCacheEntity(
                currency = currency.name,
                rateToTwd = 1.0 / apiRate,
                updatedAt = updatedAt,
            )
        }

fun List<ExchangeRateCacheEntity>.toDomain(): ExchangeRate {
    val rates = associate { Currency.valueOf(it.currency) to it.rateToTwd }
    val updatedAt = firstOrNull()?.updatedAt?.let { Instant.ofEpochMilli(it) } ?: Instant.now()
    return ExchangeRate(rates = rates, updatedAt = updatedAt)
}
