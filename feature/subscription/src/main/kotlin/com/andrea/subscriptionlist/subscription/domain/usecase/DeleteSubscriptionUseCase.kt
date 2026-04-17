package com.andrea.subscriptionlist.subscription.domain.usecase

import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import javax.inject.Inject

class DeleteSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
