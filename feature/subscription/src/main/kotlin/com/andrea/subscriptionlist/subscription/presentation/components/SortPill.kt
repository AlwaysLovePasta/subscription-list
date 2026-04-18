package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.BorderMid
import com.andrea.subscriptionlist.core.ui.theme.Cream
import com.andrea.subscriptionlist.core.ui.theme.InkDeep
import com.andrea.subscriptionlist.core.ui.theme.InkMid
import com.andrea.subscriptionlist.core.ui.theme.Parchment
import com.andrea.subscriptionlist.core.ui.theme.SubscriptionListTheme
import com.andrea.subscriptionlist.subscription.presentation.list.SortOrder

@Composable
fun SortPill(
    sortOrder: SortOrder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = when (sortOrder) {
        SortOrder.BY_NAME -> "Service name"
        SortOrder.BY_MONTHLY_COST -> "Monthly cost"
        SortOrder.BY_NEXT_BILLING -> "Next billing"
    }
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, BorderMid),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Parchment,
            contentColor = InkDeep,
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = "Sort by ",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMid,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = InkDeep,
        )
        Spacer(Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = InkMid,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SortPillPreview() {
    SubscriptionListTheme {
        Column(
            modifier = Modifier
                .background(Cream)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SortOrder.entries.forEach { order ->
                SortPill(sortOrder = order, onClick = {})
            }
        }
    }
}
