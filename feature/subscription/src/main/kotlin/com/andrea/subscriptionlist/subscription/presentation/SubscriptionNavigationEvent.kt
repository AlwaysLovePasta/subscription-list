package com.andrea.subscriptionlist.subscription.presentation

sealed interface SubscriptionNavigationEvent {
    data object NavigateToAdd : SubscriptionNavigationEvent
    data class NavigateToEdit(val subscriptionId: String) : SubscriptionNavigationEvent
}
