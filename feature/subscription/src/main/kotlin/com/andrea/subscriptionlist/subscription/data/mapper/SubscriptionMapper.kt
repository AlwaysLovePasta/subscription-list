package com.andrea.subscriptionlist.subscription.data.mapper

import com.andrea.subscriptionlist.subscription.data.local.SubscriptionEntity
import com.andrea.subscriptionlist.subscription.domain.model.Currency
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import java.time.LocalDate

fun SubscriptionEntity.toDomain() = Subscription(
    id = id,
    serviceName = serviceName,
    planName = planName,
    price = price,
    currency = Currency.valueOf(currency),
    billingCycleMonths = billingCycleMonths,
    nextBillingDate = LocalDate.ofEpochDay(nextBillingDateEpochDay),
)

fun Subscription.toEntity() = SubscriptionEntity(
    id = id,
    serviceName = serviceName,
    planName = planName,
    price = price,
    currency = currency.name,
    billingCycleMonths = billingCycleMonths,
    nextBillingDateEpochDay = nextBillingDate.toEpochDay(),
)
