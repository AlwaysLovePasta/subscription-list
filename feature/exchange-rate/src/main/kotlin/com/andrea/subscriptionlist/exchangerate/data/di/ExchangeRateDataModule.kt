package com.andrea.subscriptionlist.exchangerate.data.di

import android.content.Context
import androidx.room.Room
import com.andrea.subscriptionlist.exchangerate.data.local.ExchangeRateCacheDao
import com.andrea.subscriptionlist.exchangerate.data.local.ExchangeRateDatabase
import com.andrea.subscriptionlist.exchangerate.data.remote.ExchangeRateApi
import com.andrea.subscriptionlist.exchangerate.data.repository.ExchangeRateRepositoryImpl
import com.andrea.subscriptionlist.exchangerate.domain.repository.ExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExchangeRateDataModule {

    @Binds
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    companion object {

        private const val EXCHANGE_RATE_API_KEY = "2e2652ba957eb530562dc4f0"

        @Provides
        @Singleton
        fun provideExchangeRateDatabase(@ApplicationContext context: Context): ExchangeRateDatabase =
            Room.databaseBuilder(context, ExchangeRateDatabase::class.java, "exchange_rate.db").build()

        @Provides
        fun provideExchangeRateCacheDao(db: ExchangeRateDatabase): ExchangeRateCacheDao =
            db.exchangeRateCacheDao()

        @Provides
        @Singleton
        fun provideExchangeRateApi(): ExchangeRateApi {
            val json = Json { ignoreUnknownKeys = true }
            return Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/$EXCHANGE_RATE_API_KEY/")
                .addConverterFactory(json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
                .build()
                .create(ExchangeRateApi::class.java)
        }
    }
}
