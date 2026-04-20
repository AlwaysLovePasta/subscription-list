package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor

@Composable
internal fun SubscriptionLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ThemeColor.Cream),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = ThemeColor.AccentGreen)
    }
}
