package com.andrea.subscriptionlist.subscription.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate_cache")
data class ExchangeRateCacheEntity(
    @PrimaryKey val currency: String,
    val rateToTwd: Double,
    val updatedAt: Long,
)
