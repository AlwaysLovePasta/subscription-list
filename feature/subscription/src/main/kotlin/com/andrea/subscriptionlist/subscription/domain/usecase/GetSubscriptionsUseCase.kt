package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(): Flow<List<Subscription>> = repository.getAll()
}
