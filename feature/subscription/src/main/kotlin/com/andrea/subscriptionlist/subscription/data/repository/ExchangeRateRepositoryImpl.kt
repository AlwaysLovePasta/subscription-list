package com.andrea.subscriptionlist.subscription.data.repository

import com.andrea.subscriptionlist.subscription.data.local.ExchangeRateCacheDao
import com.andrea.subscriptionlist.subscription.data.mapper.toDomain
import com.andrea.subscriptionlist.subscription.data.mapper.toEntities
import com.andrea.subscriptionlist.subscription.data.remote.ExchangeRateApi
import com.andrea.subscriptionlist.subscription.domain.model.ExchangeRate
import com.andrea.subscriptionlist.subscription.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CACHE_TTL_MS = 24 * 60 * 60 * 1000L

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi,
    private val cacheDao: ExchangeRateCacheDao,
) : ExchangeRateRepository {

    override suspend fun getLatest(): ExchangeRate = withContext(Dispatchers.IO) {
        val cached = cacheDao.getAll()
        if (cached.isNotEmpty() && !isCacheStale(cached.first().updatedAt)) {
            return@withContext cached.toDomain()
        }
        val now = System.currentTimeMillis()
        val entities = api.getLatestRates().toEntities(now)
        cacheDao.deleteAll()
        cacheDao.insertAll(entities)
        entities.toDomain()
    }

    private fun isCacheStale(updatedAt: Long): Boolean =
        System.currentTimeMillis() - updatedAt > CACHE_TTL_MS
}
