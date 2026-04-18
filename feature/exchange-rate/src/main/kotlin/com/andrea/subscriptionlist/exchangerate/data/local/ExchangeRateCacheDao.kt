package com.andrea.subscriptionlist.exchangerate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExchangeRateCacheDao {

    @Query("SELECT * FROM exchange_rate_cache")
    suspend fun getAll(): List<ExchangeRateCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ExchangeRateCacheEntity>)

    @Query("DELETE FROM exchange_rate_cache")
    suspend fun deleteAll()
}
