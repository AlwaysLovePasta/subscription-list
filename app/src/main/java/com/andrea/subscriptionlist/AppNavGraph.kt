package com.andrea.subscriptionlist

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andrea.subscriptionlist.subscription.presentation.screens.SubscriptionFormScreen
import com.andrea.subscriptionlist.subscription.presentation.screens.SubscriptionListScreen
import kotlinx.serialization.Serializable

@Serializable data object SubscriptionListRoute
@Serializable data class SubscriptionFormRoute(val subscriptionId: String? = null)

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = SubscriptionListRoute) {
        composable<SubscriptionListRoute> {
            SubscriptionListScreen(
                onNavigateToAdd = { navController.navigate(SubscriptionFormRoute()) },
                onNavigateToEdit = { id -> navController.navigate(SubscriptionFormRoute(id)) },
            )
        }
        composable<SubscriptionFormRoute> {
            SubscriptionFormScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
