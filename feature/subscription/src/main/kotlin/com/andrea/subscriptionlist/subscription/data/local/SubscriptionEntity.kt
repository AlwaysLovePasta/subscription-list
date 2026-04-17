package com.andrea.subscriptionlist.subscription.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val id: String,
    val serviceName: String,
    val planName: String,
    val price: Double,
    val currency: String,
    val billingCycleMonths: Int,
    val nextBillingDateEpochDay: Long,
)
