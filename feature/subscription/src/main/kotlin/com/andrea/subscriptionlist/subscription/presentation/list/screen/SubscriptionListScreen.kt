package com.andrea.subscriptionlist.subscription.presentation.list.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.andrea.subscriptionlist.core.ui.theme.AccentGreen
import com.andrea.subscriptionlist.core.ui.theme.Cream
import com.andrea.subscriptionlist.core.ui.theme.InkDeep
import com.andrea.subscriptionlist.core.ui.theme.InkMid
import com.andrea.subscriptionlist.core.ui.theme.Parchment
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionListNavigationEvent
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionListUiEvent
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionListUiState
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionListViewModel
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionEmptyContent
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionErrorContent
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionListContent
import com.andrea.subscriptionlist.subscription.presentation.list.SubscriptionLoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionListScreen(
    onNavigateToAdd: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {},
    viewModel: SubscriptionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.navigationEvent, lifecycleOwner) {
        viewModel.navigationEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    SubscriptionListNavigationEvent.NavigateToAdd -> onNavigateToAdd()
                    is SubscriptionListNavigationEvent.NavigateToEdit -> onNavigateToEdit(event.subscriptionId)
                }
            }
    }

    val showFab = uiState is SubscriptionListUiState.Success &&
        (uiState as SubscriptionListUiState.Success).items.isNotEmpty()

    val itemCount = (uiState as? SubscriptionListUiState.Success)?.items?.size

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Subscriptions",
                            style = MaterialTheme.typography.headlineMedium,
                            color = InkDeep,
                        )
                        if (itemCount != null && itemCount > 0) {
                            Text(
                                text = "$itemCount active",
                                style = MaterialTheme.typography.bodySmall,
                                color = InkMid,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream),
            )
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(SubscriptionListUiEvent.NavigateToAdd) },
                    containerColor = AccentGreen,
                    contentColor = Parchment,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add subscription")
                }
            }
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is SubscriptionListUiState.Loading -> SubscriptionLoadingContent(
                modifier = Modifier.padding(innerPadding),
            )
            is SubscriptionListUiState.Error -> SubscriptionErrorContent(
                message = state.message,
                modifier = Modifier.padding(innerPadding),
            )
            is SubscriptionListUiState.Success -> {
                if (state.items.isEmpty()) {
                    SubscriptionEmptyContent(
                        onAddClick = { viewModel.onEvent(SubscriptionListUiEvent.NavigateToAdd) },
                        modifier = Modifier.padding(innerPadding),
                    )
                } else {
                    SubscriptionListContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        contentPadding = innerPadding,
                    )
                }
            }
        }
    }
}
