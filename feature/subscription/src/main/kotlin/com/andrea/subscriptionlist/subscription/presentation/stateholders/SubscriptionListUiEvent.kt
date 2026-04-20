package com.andrea.subscriptionlist.subscription.presentation.stateholders

sealed interface SubscriptionListUiEvent {
    data object Refresh : SubscriptionListUiEvent
    data object NavigateToAdd : SubscriptionListUiEvent
    data class NavigateToEdit(val subscriptionId: String) : SubscriptionListUiEvent
    data class DeleteSubscription(val subscriptionId: String) : SubscriptionListUiEvent
    data class ChangeSortOrder(val order: SortOrder) : SubscriptionListUiEvent
    data object DismissError : SubscriptionListUiEvent
}
