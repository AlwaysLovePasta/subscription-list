package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.subscription.domain.model.Subscription
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(subscription: Subscription) = repository.add(subscription)
}
