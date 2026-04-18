package com.andrea.subscriptionlist.subscription.data.repository

import com.andrea.subscriptionlist.subscription.data.local.SubscriptionDao
import com.andrea.subscriptionlist.subscription.data.mapper.toDomain
import com.andrea.subscriptionlist.subscription.data.mapper.toEntity
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val dao: SubscriptionDao,
) : SubscriptionRepository {

    override fun getAll(): Flow<List<Subscription>> =
        dao.observeAll()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)

    override suspend fun add(subscription: Subscription) =
        withContext(Dispatchers.IO) { dao.upsert(subscription.toEntity()) }

    override suspend fun update(subscription: Subscription) =
        withContext(Dispatchers.IO) { dao.upsert(subscription.toEntity()) }

    override suspend fun delete(id: String) =
        withContext(Dispatchers.IO) { dao.deleteById(id) }
}
