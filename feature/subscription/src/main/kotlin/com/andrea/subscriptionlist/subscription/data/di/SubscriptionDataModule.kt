package com.andrea.subscriptionlist.subscription.data.di

import android.content.Context
import androidx.room.Room
import com.andrea.subscriptionlist.subscription.data.local.AppDatabase
import com.andrea.subscriptionlist.subscription.data.local.ExchangeRateCacheDao
import com.andrea.subscriptionlist.subscription.data.local.SubscriptionDao
import com.andrea.subscriptionlist.subscription.data.remote.ExchangeRateApi
import com.andrea.subscriptionlist.subscription.data.repository.ExchangeRateRepositoryImpl
import com.andrea.subscriptionlist.subscription.data.repository.SubscriptionRepositoryImpl
import com.andrea.subscriptionlist.subscription.domain.repository.ExchangeRateRepository
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
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
abstract class SubscriptionDataModule {

    @Binds
    abstract fun bindSubscriptionRepository(impl: SubscriptionRepositoryImpl): SubscriptionRepository

    @Binds
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    companion object {

        private const val EXCHANGE_RATE_API_KEY = "2e2652ba957eb530562dc4f0"

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "subscription_list.db").build()

        @Provides
        fun provideSubscriptionDao(db: AppDatabase): SubscriptionDao = db.subscriptionDao()

        @Provides
        fun provideExchangeRateCacheDao(db: AppDatabase): ExchangeRateCacheDao = db.exchangeRateCacheDao()

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
