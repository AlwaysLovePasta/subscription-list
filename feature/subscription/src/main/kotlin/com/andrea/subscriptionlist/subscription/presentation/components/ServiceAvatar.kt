package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.Cream
import com.andrea.subscriptionlist.core.ui.theme.Parchment
import com.andrea.subscriptionlist.core.ui.theme.SubscriptionListTheme
import kotlin.math.abs

private val AvatarPalette = listOf(
    Color(0xFFC4A87A),
    Color(0xFFA08060),
    Color(0xFF8C7458),
    Color(0xFFB09B70),
    Color(0xFF9A8864),
)

@Composable
fun ServiceAvatar(
    serviceName: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    val bgColor = remember(serviceName) {
        AvatarPalette[abs(serviceName.hashCode()) % AvatarPalette.size]
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = serviceName.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = Parchment,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ServiceAvatarPreview() {
    SubscriptionListTheme {
        Row(
            modifier = Modifier
                .background(Cream)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Netflix", "Spotify", "Adobe", "YouTube", "iCloud").forEach { name ->
                ServiceAvatar(serviceName = name)
            }
        }
    }
}
