package com.andrea.subscriptionlist.exchangerate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExchangeRateCacheEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateCacheDao(): ExchangeRateCacheDao
}
