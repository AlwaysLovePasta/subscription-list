package com.andrea.subscriptionlist.subscription.data.remote

import retrofit2.http.GET

interface ExchangeRateApi {

    @GET("latest/TWD")
    suspend fun getLatestRates(): ExchangeRateDto
}
