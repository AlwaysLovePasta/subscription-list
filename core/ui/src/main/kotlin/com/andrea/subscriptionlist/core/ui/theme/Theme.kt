package com.andrea.subscriptionlist.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SubscriptionListTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SubscriptionColorScheme,
        typography = SubscriptionTypography,
        content = content,
    )
}
