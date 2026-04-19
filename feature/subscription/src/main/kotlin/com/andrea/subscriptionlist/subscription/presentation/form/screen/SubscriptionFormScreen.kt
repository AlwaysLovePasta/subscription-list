package com.andrea.subscriptionlist.subscription.presentation.form.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.core.ui.theme.ThemeColor
import com.andrea.subscriptionlist.subscription.presentation.components.DeleteConfirmationDialog
import com.andrea.subscriptionlist.subscription.presentation.form.BillingCycleSelector
import com.andrea.subscriptionlist.subscription.presentation.form.CurrencyDropdown
import com.andrea.subscriptionlist.subscription.presentation.form.DateFieldButton
import com.andrea.subscriptionlist.subscription.presentation.form.FormMode
import com.andrea.subscriptionlist.subscription.presentation.form.MonthlyAverageCard
import com.andrea.subscriptionlist.subscription.presentation.form.SubscriptionFormUiEvent
import com.andrea.subscriptionlist.subscription.presentation.form.SubscriptionFormViewModel
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: SubscriptionFormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isDone) {
        if (state.isDone) onNavigateBack()
    }

    val title = if (state.mode == FormMode.ADD) "New subscription" else "Edit subscription"

    val breakdown = rememberBreakdown(state.price, state.currency, state.exchangeRateToTwd, state.billingCycleMonths)

    Scaffold(
        containerColor = ThemeColor.Cream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = ThemeColor.InkDeep,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.onEvent(SubscriptionFormUiEvent.Save) },
                        enabled = state.isFormValid && !state.isSaving,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ThemeColor.AccentGreen,
                            contentColor = ThemeColor.Parchment,
                            disabledContainerColor = ThemeColor.BorderSubtle,
                            disabledContentColor = ThemeColor.InkMid,
                        ),
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text("Save", style = MaterialTheme.typography.bodyLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.Cream),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MonthlyAverageCard(
                monthlyAmountTwd = state.monthlyAmountTwd,
                breakdown = breakdown,
            )

            FormField(label = "Service name") {
                OutlinedTextField(
                    value = state.serviceName,
                    onValueChange = { viewModel.onEvent(SubscriptionFormUiEvent.ServiceNameChanged(it)) },
                    placeholder = { Text("e.g. Netflix", color = ThemeColor.InkMid) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            FormField(label = "Plan name") {
                OutlinedTextField(
                    value = state.planName,
                    onValueChange = { viewModel.onEvent(SubscriptionFormUiEvent.PlanNameChanged(it)) },
                    placeholder = { Text("e.g. Premium 4K", color = ThemeColor.InkMid) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FormField(label = "Price", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = state.price,
                        onValueChange = { viewModel.onEvent(SubscriptionFormUiEvent.PriceChanged(it)) },
                        placeholder = { Text("0.00", color = ThemeColor.InkMid) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FormField(label = "Currency", modifier = Modifier.width(140.dp)) {
                    CurrencyDropdown(
                        selected = state.currency,
                        onSelected = { viewModel.onEvent(SubscriptionFormUiEvent.CurrencyChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            FormField(label = "Billing cycle") {
                BillingCycleSelector(
                    selectedMonths = state.billingCycleMonths,
                    onMonthsChanged = { viewModel.onEvent(SubscriptionFormUiEvent.BillingCycleChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            FormField(label = "Next billing date") {
                DateFieldButton(
                    date = state.nextBillingDate,
                    onDateSelected = { viewModel.onEvent(SubscriptionFormUiEvent.NextBillingDateChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (state.mode == FormMode.EDIT) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { viewModel.onEvent(SubscriptionFormUiEvent.Delete) },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, ThemeColor.Danger),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeColor.Danger),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text("Delete subscription", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            serviceName = state.serviceName,
            planName = state.planName,
            monthlyAmountTwd = state.monthlyAmountTwd,
            onConfirm = { viewModel.onEvent(SubscriptionFormUiEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(SubscriptionFormUiEvent.DismissDeleteConfirmation) },
        )
    }
}

@Composable
private fun FormField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "$label *",
            style = MaterialTheme.typography.bodyMedium,
            color = ThemeColor.InkDeep,
        )
        content()
    }
}

@Composable
private fun rememberBreakdown(
    priceStr: String,
    currency: Currency,
    rateToTwd: Double?,
    billingCycleMonths: Int,
): String? {
    val price = priceStr.toDoubleOrNull() ?: return null
    if (price <= 0 || rateToTwd == null || currency == Currency.TWD) return null
    val nf = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 2 }
    val rateNf = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 1 }
    return remember { "${currency.name} ${nf.format(price)} × ${rateNf.format(rateToTwd)} ÷ $billingCycleMonths mo" }
}
