package com.andrea.subscriptionlist.subscription.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeRateCacheDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ExchangeRateCacheDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.exchangeRateCacheDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun insertAll_then_getAll_returns_inserted_entries() = runTest {
        dao.insertAll(listOf(
            ExchangeRateCacheEntity("USD", 32.5, 1000L),
            ExchangeRateCacheEntity("EUR", 35.0, 1000L),
        ))

        val result = dao.getAll()
        assertEquals(2, result.size)
    }

    @Test
    fun insertAll_replaces_entry_with_same_currency() = runTest {
        dao.insertAll(listOf(ExchangeRateCacheEntity("USD", 32.5, 1000L)))
        dao.insertAll(listOf(ExchangeRateCacheEntity("USD", 33.0, 2000L)))

        val result = dao.getAll()
        assertEquals(1, result.size)
        assertEquals(33.0, result.first().rateToTwd, 0.0001)
    }

    @Test
    fun deleteAll_removes_all_entries() = runTest {
        dao.insertAll(listOf(
            ExchangeRateCacheEntity("USD", 32.5, 1000L),
            ExchangeRateCacheEntity("EUR", 35.0, 1000L),
        ))
        dao.deleteAll()

        assertTrue(dao.getAll().isEmpty())
    }

    @Test
    fun getAll_returns_empty_when_no_data() = runTest {
        assertTrue(dao.getAll().isEmpty())
    }
}
