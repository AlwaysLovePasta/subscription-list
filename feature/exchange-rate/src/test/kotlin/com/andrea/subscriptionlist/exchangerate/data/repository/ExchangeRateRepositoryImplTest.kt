package com.andrea.subscriptionlist.exchangerate.data.repository

import com.andrea.subscriptionlist.core.common.Currency
import com.andrea.subscriptionlist.exchangerate.data.local.ExchangeRateCacheDao
import com.andrea.subscriptionlist.exchangerate.data.local.ExchangeRateCacheEntity
import com.andrea.subscriptionlist.exchangerate.data.remote.ExchangeRateApi
import com.andrea.subscriptionlist.exchangerate.data.remote.ExchangeRateDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class ExchangeRateRepositoryImplTest {

    private val api: ExchangeRateApi = mockk()
    private val cacheDao: ExchangeRateCacheDao = mockk()
    private lateinit var repository: ExchangeRateRepositoryImpl

    private val stubDto = ExchangeRateDto(
        baseCode = "TWD",
        conversionRates = mapOf("USD" to 0.031, "EUR" to 0.028, "TWD" to 1.0),
    )

    @BeforeEach
    fun setUp() {
        repository = ExchangeRateRepositoryImpl(api, cacheDao)
    }

    @Test
    fun `empty cache calls API and caches result`() = runTest {
        coEvery { cacheDao.getAll() } returns emptyList()
        coEvery { api.getLatestRates() } returns stubDto
        coEvery { cacheDao.deleteAll() } just Runs
        coEvery { cacheDao.insertAll(any()) } just Runs

        repository.getLatest()

        coVerify(exactly = 1) { api.getLatestRates() }
        coVerify(exactly = 1) { cacheDao.deleteAll() }
        coVerify(exactly = 1) { cacheDao.insertAll(any()) }
    }

    @Test
    fun `fresh cache returns cached data without calling API`() = runTest {
        val freshCache = listOf(
            ExchangeRateCacheEntity("USD", 32.5, System.currentTimeMillis()),
            ExchangeRateCacheEntity("EUR", 35.0, System.currentTimeMillis()),
        )
        coEvery { cacheDao.getAll() } returns freshCache

        val result = repository.getLatest()

        coVerify(exactly = 0) { api.getLatestRates() }
        assertEquals(32.5, result.rates[Currency.USD])
    }

    @Test
    fun `stale cache calls API and replaces cache`() = runTest {
        val staleCache = listOf(
            ExchangeRateCacheEntity("USD", 32.5, 0L),
        )
        coEvery { cacheDao.getAll() } returns staleCache
        coEvery { api.getLatestRates() } returns stubDto
        coEvery { cacheDao.deleteAll() } just Runs
        coEvery { cacheDao.insertAll(any()) } just Runs

        repository.getLatest()

        coVerify(exactly = 1) { api.getLatestRates() }
        coVerify(exactly = 1) { cacheDao.deleteAll() }
    }

    @Test
    fun `API throws exception propagates to caller`() = runTest {
        coEvery { cacheDao.getAll() } returns emptyList()
        coEvery { api.getLatestRates() } throws IOException("network error")

        assertThrows<IOException> { repository.getLatest() }
    }
}
