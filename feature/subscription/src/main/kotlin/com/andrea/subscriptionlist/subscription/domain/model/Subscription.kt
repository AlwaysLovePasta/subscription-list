package com.andrea.subscriptionlist.subscription.domain.model

import java.time.LocalDate

data class Subscription(
    val id: String,
    val serviceName: String,
    val planName: String,
    val price: Double,
    val currency: Currency,
    val billingCycleMonths: Int,
    val nextBillingDate: LocalDate,
)
