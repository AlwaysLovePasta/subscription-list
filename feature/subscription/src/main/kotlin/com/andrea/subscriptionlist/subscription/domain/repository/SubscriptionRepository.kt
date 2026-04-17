package com.andrea.subscriptionlist.subscription.domain.repository

import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getAll(): Flow<List<Subscription>>
    suspend fun add(subscription: Subscription)
    suspend fun update(subscription: Subscription)
    suspend fun delete(id: String)
}
