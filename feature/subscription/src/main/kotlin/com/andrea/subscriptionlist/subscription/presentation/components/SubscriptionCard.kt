package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.LinearProgressIndicator
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
import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.subscription.presentation.stateholders.SubscriptionItemUiModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val BillingDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@Composable
fun SubscriptionCard(
    item: SubscriptionItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = remember { LocalDate.now() }
    val daysUntil = remember(item.nextBillingDate) {
        ChronoUnit.DAYS.between(today, item.nextBillingDate).coerceAtLeast(0)
    }
    val billingProgress = remember(daysUntil, item.billingCycleMonths) {
        val total = (item.billingCycleMonths * 30).toLong()
        if (total > 0) ((total - daysUntil).toFloat() / total).coerceIn(0f, 1f) else 1f
    }
    val amountText = remember(item.monthlyAmountTwd) {
        val nf = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 0 }
        item.monthlyAmountTwd?.let { "NT$${nf.format(it)}" } ?: "NT$—"
    }
    val priceText = remember(item.price, item.currency, item.billingCycleMonths) {
        "${item.currency.name} ${"%.2f".format(item.price / item.billingCycleMonths)}/mo"
    }
    val daysText = remember(daysUntil, item.nextBillingDate) {
        "${daysUntil}d until ${item.nextBillingDate.format(BillingDateFormatter)}"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeColor.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ServiceAvatar(serviceName = item.serviceName)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.serviceName,
                        style = MaterialTheme.typography.titleSmall,
                        color = ThemeColor.InkDeep,
                    )
                    Text(
                        text = item.planName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ThemeColor.InkMid,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = amountText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic,
                        color = ThemeColor.InkDeep,
                    )
                    Text(
                        text = "PER MONTH",
                        style = MaterialTheme.typography.labelMedium,
                        color = ThemeColor.InkLight,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { billingProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = ThemeColor.AccentGreen,
                trackColor = ThemeColor.BorderSubtle,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.labelSmall,
                    color = ThemeColor.InkLight,
                )
                Text(
                    text = daysText,
                    style = MaterialTheme.typography.labelSmall,
                    color = ThemeColor.InkLight,
                )
            }
        }
    }
}

private val PreviewItems = listOf(
    SubscriptionItemUiModel(
        id = "1",
        serviceName = "Spotify",
        planName = "Individual",
        price = 149.0,
        currency = Currency.TWD,
        billingCycleMonths = 1,
        nextBillingDate = LocalDate.now().plusDays(6),
        monthlyAmountTwd = 149.0,
    ),
    SubscriptionItemUiModel(
        id = "2",
        serviceName = "Netflix",
        planName = "Premium 4K",
        price = 22.99,
        currency = Currency.USD,
        billingCycleMonths = 1,
        nextBillingDate = LocalDate.now().plusDays(13),
        monthlyAmountTwd = 747.0,
    ),
    SubscriptionItemUiModel(
        id = "3",
        serviceName = "Adobe Creative",
        planName = "All Apps",
        price = 59.99,
        currency = Currency.USD,
        billingCycleMonths = 1,
        nextBillingDate = LocalDate.now().plusDays(12),
        monthlyAmountTwd = null,
    ),
)

@Preview(showBackground = true)
@Composable
private fun SubscriptionCardPreview() {
    SubscriptionListTheme {
        Column(
            modifier = Modifier
                .background(ThemeColor.Cream)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PreviewItems.forEach { item ->
                SubscriptionCard(item = item, onClick = {})
            }
        }
    }
}
