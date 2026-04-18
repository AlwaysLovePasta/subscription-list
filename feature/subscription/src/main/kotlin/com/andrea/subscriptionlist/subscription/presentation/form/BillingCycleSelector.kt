package com.andrea.subscriptionlist.subscription.presentation.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andrea.subscriptionlist.core.ui.theme.AccentGreen
import com.andrea.subscriptionlist.core.ui.theme.BorderSubtle
import com.andrea.subscriptionlist.core.ui.theme.InkDeep
import com.andrea.subscriptionlist.core.ui.theme.Parchment

private val presets = listOf(1 to "Monthly", 3 to "Quarterly", 6 to "Half-yearly", 12 to "Yearly")

@Composable
internal fun BillingCycleSelector(
    selectedMonths: Int,
    onMonthsChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            presets.forEach { (months, label) ->
                val selected = selectedMonths == months
                FilterChip(
                    selected = selected,
                    onClick = { onMonthsChanged(months) },
                    label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentGreen,
                        selectedLabelColor = Parchment,
                        containerColor = Parchment,
                        labelColor = InkDeep,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = BorderSubtle,
                        selectedBorderColor = AccentGreen,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            OutlinedTextField(
                value = selectedMonths.toString(),
                onValueChange = { it.toIntOrNull()?.takeIf { v -> v >= 1 }?.let(onMonthsChanged) },
                modifier = Modifier.width(100.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                singleLine = true,
                trailingIcon = {
                    Column {
                        IconButton(
                            onClick = { onMonthsChanged(selectedMonths + 1) },
                            modifier = Modifier.weight(1f, fill = false),
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        }
                        IconButton(
                            onClick = { if (selectedMonths > 1) onMonthsChanged(selectedMonths - 1) },
                            modifier = Modifier.weight(1f, fill = false),
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                },
            )
            Text("mo", style = MaterialTheme.typography.bodyLarge, color = InkDeep)
        }
    }
}
