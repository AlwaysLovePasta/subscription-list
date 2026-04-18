package com.andrea.subscriptionlist.subscription.presentation

sealed interface SubscriptionUiEvent {
    data object Refresh : SubscriptionUiEvent
    data object NavigateToAdd : SubscriptionUiEvent
    data class NavigateToEdit(val subscriptionId: String) : SubscriptionUiEvent
    data class DeleteSubscription(val subscriptionId: String) : SubscriptionUiEvent
    data class ChangeSortOrder(val order: SortOrder) : SubscriptionUiEvent
    data object DismissError : SubscriptionUiEvent
}
