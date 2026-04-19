package com.andrea.subscriptionlist.subscription.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor

@Composable
internal fun SubscriptionErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ThemeColor.Cream)
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message.ifBlank { "Something went wrong." },
            style = MaterialTheme.typography.bodyLarge,
            color = ThemeColor.InkMid,
            textAlign = TextAlign.Center,
        )
    }
}
