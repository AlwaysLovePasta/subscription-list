package com.andrea.subscriptionlist.subscription.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateDto(
    @SerialName("base_code") val baseCode: String,
    @SerialName("conversion_rates") val conversionRates: Map<String, Double>,
)
