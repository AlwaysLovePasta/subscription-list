package com.andrea.subscriptionlist.subscription.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubscriptionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: SubscriptionDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.subscriptionDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun upsert_then_observeAll_emits_inserted_item() = runTest {
        dao.upsert(sampleEntity())

        dao.observeAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("id-1", items.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun upsert_with_same_id_replaces_existing_entry() = runTest {
        dao.upsert(sampleEntity(serviceName = "Netflix"))
        dao.upsert(sampleEntity(serviceName = "Netflix 4K"))

        dao.observeAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Netflix 4K", items.first().serviceName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteById_removes_matching_entry() = runTest {
        dao.upsert(sampleEntity())
        dao.deleteById("id-1")

        dao.observeAll().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteById_does_not_affect_other_entries() = runTest {
        dao.upsert(sampleEntity(id = "id-1"))
        dao.upsert(sampleEntity(id = "id-2"))
        dao.deleteById("id-1")

        dao.observeAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("id-2", items.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun sampleEntity(
        id: String = "id-1",
        serviceName: String = "Netflix",
    ) = SubscriptionEntity(
        id = id,
        serviceName = serviceName,
        planName = "Standard",
        price = 22.99,
        currency = "USD",
        billingCycleMonths = 1,
        nextBillingDateEpochDay = 20_000L,
    )
}
