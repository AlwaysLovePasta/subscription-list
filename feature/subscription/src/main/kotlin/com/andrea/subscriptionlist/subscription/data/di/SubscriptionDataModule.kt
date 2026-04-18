package com.andrea.subscriptionlist.subscription.data.di

import android.content.Context
import androidx.room.Room
import com.andrea.subscriptionlist.subscription.data.local.AppDatabase
import com.andrea.subscriptionlist.subscription.data.local.SubscriptionDao
import com.andrea.subscriptionlist.subscription.data.repository.SubscriptionRepositoryImpl
import com.andrea.subscriptionlist.subscription.domain.repository.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionDataModule {

    @Binds
    abstract fun bindSubscriptionRepository(impl: SubscriptionRepositoryImpl): SubscriptionRepository

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "subscription_list.db").build()

        @Provides
        fun provideSubscriptionDao(db: AppDatabase): SubscriptionDao = db.subscriptionDao()
    }
}
