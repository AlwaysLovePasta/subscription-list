package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.domain.model.ExchangeRate
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class CalculateMonthlyAverageUseCaseTest {

    private lateinit var useCase: CalculateMonthlyAverageUseCase

    private val stubRate = ExchangeRate(
        rates = mapOf(Currency.USD to 32.5, Currency.EUR to 35.0),
        updatedAt = Instant.EPOCH,
    )

    @BeforeEach
    fun setUp() {
        useCase = CalculateMonthlyAverageUseCase()
    }

    @Test
    fun `TWD subscription returns price divided by billing cycle without conversion`() {
        val subscription = buildSubscription(price = 180.0, currency = Currency.TWD, billingCycleMonths = 1)

        val result = useCase(subscription, stubRate)

        assertEquals(180.0, result)
    }

    @Test
    fun `USD monthly subscription converts using exchange rate`() {
        val subscription = buildSubscription(price = 10.0, currency = Currency.USD, billingCycleMonths = 1)

        val result = useCase(subscription, stubRate)

        assertEquals(325.0, result)
    }

    @Test
    fun `USD yearly subscription divides converted price by 12`() {
        val subscription = buildSubscription(price = 120.0, currency = Currency.USD, billingCycleMonths = 12)

        val result = useCase(subscription, stubRate)

        assertEquals(120.0 * 32.5 / 12, result)
    }

    @Test
    fun `EUR subscription converts using EUR rate`() {
        val subscription = buildSubscription(price = 10.0, currency = Currency.EUR, billingCycleMonths = 1)

        val result = useCase(subscription, stubRate)

        assertEquals(350.0, result)
    }

    @Test
    fun `returns null when currency rate is missing from ExchangeRate`() {
        val rateWithoutEur = ExchangeRate(
            rates = mapOf(Currency.USD to 32.5),
            updatedAt = Instant.EPOCH,
        )
        val subscription = buildSubscription(price = 10.0, currency = Currency.EUR, billingCycleMonths = 1)

        val result = useCase(subscription, rateWithoutEur)

        assertNull(result)
    }

    private fun buildSubscription(
        price: Double,
        currency: Currency,
        billingCycleMonths: Int,
    ) = Subscription(
        id = "test-id",
        serviceName = "Test Service",
        planName = "Test Plan",
        price = price,
        currency = currency,
        billingCycleMonths = billingCycleMonths,
        nextBillingDate = LocalDate.of(2026, 5, 1),
    )
}
