package com.andrea.subscriptionlist.subscription.data.repository

import com.andrea.subscriptionlist.subscription.data.local.SubscriptionDao
import com.andrea.subscriptionlist.subscription.data.mapper.toDomain
import com.andrea.subscriptionlist.subscription.data.mapper.toEntity
import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val dao: SubscriptionDao,
) : SubscriptionRepository {

    override fun getAll(): Flow<List<Subscription>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun add(subscription: Subscription) = dao.upsert(subscription.toEntity())

    override suspend fun update(subscription: Subscription) = dao.upsert(subscription.toEntity())

    override suspend fun delete(id: String) = dao.deleteById(id)
}
