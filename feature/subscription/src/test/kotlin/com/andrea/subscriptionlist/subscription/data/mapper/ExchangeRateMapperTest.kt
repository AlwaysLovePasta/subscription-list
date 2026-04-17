package com.andrea.subscriptionlist.subscription.data.mapper

import com.andrea.subscriptionlist.subscription.data.local.ExchangeRateCacheEntity
import com.andrea.subscriptionlist.subscription.data.remote.ExchangeRateDto
import com.andrea.subscriptionlist.subscription.domain.model.Currency
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class ExchangeRateMapperTest {

    private val stubDto = ExchangeRateDto(
        baseCode = "TWD",
        conversionRates = mapOf("USD" to 0.031, "EUR" to 0.028, "TWD" to 1.0),
    )

    @Test
    fun `toEntities - USD and EUR rates are inverted correctly`() {
        val result = stubDto.toEntities(updatedAt = 1000L)

        val usd = result.first { it.currency == "USD" }
        val eur = result.first { it.currency == "EUR" }
        assertEquals(1.0 / 0.031, usd.rateToTwd, 0.0001)
        assertEquals(1.0 / 0.028, eur.rateToTwd, 0.0001)
    }

    @Test
    fun `toEntities - TWD is excluded from result`() {
        val result = stubDto.toEntities(updatedAt = 1000L)

        assertNull(result.find { it.currency == "TWD" })
    }

    @Test
    fun `toEntities - currency missing from API response is skipped`() {
        val dtoWithoutEur = ExchangeRateDto(
            baseCode = "TWD",
            conversionRates = mapOf("USD" to 0.031),
        )

        val result = dtoWithoutEur.toEntities(updatedAt = 1000L)

        assertNull(result.find { it.currency == "EUR" })
        assertEquals(1, result.size)
    }

    @Test
    fun `toEntities - zero rate is skipped`() {
        val dtoWithZero = ExchangeRateDto(
            baseCode = "TWD",
            conversionRates = mapOf("USD" to 0.0),
        )

        val result = dtoWithZero.toEntities(updatedAt = 1000L)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntities - updatedAt is propagated to all entities`() {
        val result = stubDto.toEntities(updatedAt = 9999L)

        assertTrue(result.all { it.updatedAt == 9999L })
    }

    @Test
    fun `toDomain - maps rates and updatedAt correctly`() {
        val entities = listOf(
            ExchangeRateCacheEntity("USD", 32.5, 1000L),
            ExchangeRateCacheEntity("EUR", 35.0, 1000L),
        )

        val result = entities.toDomain()

        assertEquals(32.5, result.rates[Currency.USD])
        assertEquals(35.0, result.rates[Currency.EUR])
        assertEquals(Instant.ofEpochMilli(1000L), result.updatedAt)
    }

    @Test
    fun `toDomain - empty list returns empty rates`() {
        val result = emptyList<ExchangeRateCacheEntity>().toDomain()

        assertTrue(result.rates.isEmpty())
    }
}
