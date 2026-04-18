package com.andrea.subscriptionlist.subscription.data.mapper

import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.subscription.data.local.SubscriptionEntity
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SubscriptionMapperTest {

    private val sampleDate = LocalDate.of(2026, 5, 1)

    private val sampleSubscription = Subscription(
        id = "id-1",
        serviceName = "Netflix",
        planName = "4K Plan",
        price = 22.99,
        currency = Currency.USD,
        billingCycleMonths = 1,
        nextBillingDate = sampleDate,
    )

    private val sampleEntity = SubscriptionEntity(
        id = "id-1",
        serviceName = "Netflix",
        planName = "4K Plan",
        price = 22.99,
        currency = "USD",
        billingCycleMonths = 1,
        nextBillingDateEpochDay = sampleDate.toEpochDay(),
    )

    @Test
    fun `toEntity - LocalDate converts to epoch day`() {
        val entity = sampleSubscription.toEntity()

        assertEquals(sampleDate.toEpochDay(), entity.nextBillingDateEpochDay)
    }

    @Test
    fun `toEntity - currency enum converts to name string`() {
        val entity = sampleSubscription.toEntity()

        assertEquals("USD", entity.currency)
    }

    @Test
    fun `toDomain - epoch day converts back to LocalDate`() {
        val domain = sampleEntity.toDomain()

        assertEquals(sampleDate, domain.nextBillingDate)
    }

    @Test
    fun `toDomain - currency string converts back to enum`() {
        val domain = sampleEntity.toDomain()

        assertEquals(Currency.USD, domain.currency)
    }

    @Test
    fun `round-trip - toEntity then toDomain equals original`() {
        val result = sampleSubscription.toEntity().toDomain()

        assertEquals(sampleSubscription, result)
    }
}
