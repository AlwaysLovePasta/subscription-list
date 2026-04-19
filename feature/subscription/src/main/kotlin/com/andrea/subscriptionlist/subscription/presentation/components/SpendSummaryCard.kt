package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor
import com.andrea.subscriptionlist.core.ui.theme.SubscriptionListTheme
import java.text.NumberFormat

@Composable
fun SpendSummaryCard(
    totalMonthlyTwd: Double,
    fxSummary: String?,
    modifier: Modifier = Modifier,
) {
    val nf = remember { NumberFormat.getNumberInstance().apply { maximumFractionDigits = 0 } }
    val monthly = remember(totalMonthlyTwd) { nf.format(totalMonthlyTwd) }
    val yearly = remember(totalMonthlyTwd) { nf.format(totalMonthlyTwd * 12) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeColor.CreamDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            Text(
                text = "EST. MONTHLY SPEND",
                style = MaterialTheme.typography.labelMedium,
                color = ThemeColor.InkMid,
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "NT$",
                    style = MaterialTheme.typography.titleLarge,
                    color = ThemeColor.InkMid,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = monthly,
                    style = MaterialTheme.typography.displayLarge,
                    fontStyle = FontStyle.Italic,
                    color = ThemeColor.InkDeep,
                )
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = ThemeColor.BorderSubtle, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "≈ NT$$yearly / year",
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColor.InkMid,
                )
                if (fxSummary != null) {
                    Text(
                        text = "FX · $fxSummary",
                        style = MaterialTheme.typography.labelSmall,
                        color = ThemeColor.InkLight,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpendSummaryCardPreview() {
    SubscriptionListTheme {
        Column(
            modifier = Modifier
                .background(ThemeColor.Cream)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SpendSummaryCard(
                totalMonthlyTwd = 4114.0,
                fxSummary = "USD 32.5 · EUR 35.2",
            )
            SpendSummaryCard(
                totalMonthlyTwd = 149.0,
                fxSummary = null,
            )
        }
    }
}
