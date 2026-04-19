package com.andrea.subscriptionlist.subscription.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor
import com.andrea.subscriptionlist.core.ui.theme.SubscriptionListTheme
import java.text.NumberFormat

@Composable
fun DeleteConfirmationDialog(
    serviceName: String,
    planName: String,
    monthlyAmountTwd: Double?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ThemeColor.Parchment),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DangerIconCircle()
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Delete subscription?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontStyle = FontStyle.Italic,
                    color = ThemeColor.InkDeep,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "$serviceName — $planName",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = ThemeColor.InkDeep,
                    textAlign = TextAlign.Center,
                )
                if (monthlyAmountTwd != null) {
                    val amountText = remember(monthlyAmountTwd) {
                        val nf = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 0 }
                        "NT$${nf.format(monthlyAmountTwd)}/mo"
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "This removes $amountText from your total.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ThemeColor.InkMid,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, ThemeColor.BorderSubtle),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeColor.InkDeep),
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ThemeColor.Danger,
                            contentColor = ThemeColor.Parchment,
                        ),
                    ) {
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DangerIconCircle() {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
            tint = ThemeColor.Danger,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationDialogPreview() {
    SubscriptionListTheme {
        Box(
            modifier = Modifier
                .background(ThemeColor.Cream)
                .padding(24.dp),
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeColor.Parchment),
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DangerIconCircle()
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Delete subscription?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontStyle = FontStyle.Italic,
                        color = ThemeColor.InkDeep,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Spotify — Individual",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = ThemeColor.InkDeep,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "This removes NT$149/mo from your total.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ThemeColor.InkMid,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, ThemeColor.BorderSubtle),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeColor.InkDeep),
                        ) { Text("Cancel", style = MaterialTheme.typography.bodyLarge) }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ThemeColor.Danger,
                                contentColor = ThemeColor.Parchment,
                            ),
                        ) { Text("Delete", style = MaterialTheme.typography.bodyLarge) }
                    }
                }
            }
        }
    }
}
