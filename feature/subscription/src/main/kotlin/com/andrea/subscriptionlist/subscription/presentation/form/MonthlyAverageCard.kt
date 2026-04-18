package com.andrea.subscriptionlist.subscription.presentation.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.AccentGreenBg
import com.andrea.subscriptionlist.core.ui.theme.InkDeep
import com.andrea.subscriptionlist.core.ui.theme.InkLight
import com.andrea.subscriptionlist.core.ui.theme.InkMid
import java.text.NumberFormat

@Composable
internal fun MonthlyAverageCard(
    monthlyAmountTwd: Double?,
    breakdown: String?,
    modifier: Modifier = Modifier,
) {
    val nf = remember { NumberFormat.getNumberInstance().apply { maximumFractionDigits = 0 } }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AccentGreenBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "MONTHLY AVERAGE (TWD)",
                style = MaterialTheme.typography.labelMedium,
                color = InkMid,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (monthlyAmountTwd != null) "≈ NT$ ${nf.format(monthlyAmountTwd)} /mo"
                       else "≈ NT$ — /mo",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                color = InkDeep,
            )
            if (breakdown != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = breakdown,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkLight,
                )
            }
        }
    }
}
