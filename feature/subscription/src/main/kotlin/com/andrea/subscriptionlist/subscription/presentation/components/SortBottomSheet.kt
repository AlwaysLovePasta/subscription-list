package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor
import com.andrea.subscriptionlist.core.ui.theme.SubscriptionListTheme
import com.andrea.subscriptionlist.subscription.presentation.list.SortOrder

private data class SortOption(
    val order: SortOrder,
    val title: String,
    val subtitle: String,
)

private val SortOptions = listOf(
    SortOption(SortOrder.BY_NAME, "Service name", "A → Z"),
    SortOption(SortOrder.BY_MONTHLY_COST, "Monthly cost", "High → Low"),
    SortOption(SortOrder.BY_NEXT_BILLING, "Next billing", "Soonest first"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentOrder: SortOrder,
    onOrderSelected: (SortOrder) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ThemeColor.Parchment,
    ) {
        SortBottomSheetContent(
            currentOrder = currentOrder,
            onOrderSelected = onOrderSelected,
        )
    }
}

@Composable
private fun SortBottomSheetContent(
    currentOrder: SortOrder,
    onOrderSelected: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(bottom = 32.dp)) {
        Text(
            text = "SORT BY",
            style = MaterialTheme.typography.labelMedium,
            color = ThemeColor.InkMid,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        )
        SortOptions.forEachIndexed { index, option ->
            SortOptionRow(
                option = option,
                isSelected = option.order == currentOrder,
                onClick = { onOrderSelected(option.order) },
            )
            if (index < SortOptions.lastIndex) {
                HorizontalDivider(
                    color = ThemeColor.BorderSubtle,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SortOptionRow(
    option: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val textColor = if (isSelected) ThemeColor.AccentGreen else ThemeColor.InkDeep
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
            )
            Text(
                text = option.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) ThemeColor.AccentGreen else ThemeColor.InkMid,
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = ThemeColor.AccentGreen,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortBottomSheetPreview() {
    SubscriptionListTheme {
        Box(modifier = Modifier.background(ThemeColor.Cream)) {
            SortBottomSheetContent(
                currentOrder = SortOrder.BY_NEXT_BILLING,
                onOrderSelected = {},
            )
        }
    }
}
