package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiEvent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiState

@Composable
internal fun SubscriptionListContent(
    state: SubscriptionListUiState.Success,
    onEvent: (SubscriptionListUiEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var showSortSheet by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
            top = contentPadding.calculateTopPadding() + 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 20.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            SpendSummaryCard(
                totalMonthlyTwd = state.totalMonthlyTwd,
                fxSummary = state.fxSummary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                SortPill(
                    sortOrder = state.sortOrder,
                    onClick = { showSortSheet = true },
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
        items(state.items, key = { it.id }) { item ->
            SubscriptionCard(
                item = item,
                onClick = { onEvent(SubscriptionListUiEvent.NavigateToEdit(item.id)) },
            )
        }
    }

    if (showSortSheet) {
        SortBottomSheet(
            currentOrder = state.sortOrder,
            onOrderSelected = { order ->
                onEvent(SubscriptionListUiEvent.ChangeSortOrder(order))
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false },
        )
    }
}
