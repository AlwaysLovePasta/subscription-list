package com.andrea.subscriptionlist.subscription.presentation.screens

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
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor
import com.andrea.subscriptionlist.subscription.presentation.components.SubscriptionEmptyContent
import com.andrea.subscriptionlist.subscription.presentation.components.SubscriptionErrorContent
import com.andrea.subscriptionlist.subscription.presentation.components.SubscriptionListContent
import com.andrea.subscriptionlist.subscription.presentation.components.SubscriptionLoadingContent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListNavigationEvent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiEvent
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListUiState
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionListViewModel

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
        containerColor = ThemeColor.Cream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Subscriptions",
                            style = MaterialTheme.typography.titleLarge,
                            color = ThemeColor.InkDeep,
                        )
                        if (itemCount != null && itemCount > 0) {
                            Text(
                                text = "$itemCount active",
                                style = MaterialTheme.typography.bodySmall,
                                color = ThemeColor.InkMid,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.Cream),
            )
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(SubscriptionListUiEvent.NavigateToAdd) },
                    containerColor = ThemeColor.AccentGreen,
                    contentColor = ThemeColor.Parchment,
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
