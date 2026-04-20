package com.andrea.subscriptionlist.subscription.presentation.stateholders

sealed interface SubscriptionListNavigationEvent {
    data object NavigateToAdd : SubscriptionListNavigationEvent
    data class NavigateToEdit(val subscriptionId: String) : SubscriptionListNavigationEvent
}
