package com.pratama.kmmcurrency.local

import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.CurrencyDaoImpl
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.testDbConnection
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class CurrencyDaoTest {

    private lateinit var currencyDb: CurrencyDatabase
    private lateinit var currencyDao: CurrencyDao

    @BeforeTest
    fun setup() = runTest {
        currencyDb = CurrencyDatabase(testDbConnection())
        currencyDao = CurrencyDaoImpl(currencyDb)

        currencyDao.clearDatabase()
        currencyDao.insertCurrencies(listOf(Currency("IDR", "Indonesia Rupiah")))
    }

    @Test
    fun `Get currencies should be success`() = runTest {
        val currencies = currencyDao.getCurrencies()
        assertNotNull(currencies, "could not retrieve currencies")
    }

    @Test
    fun `Insert currencies should be success`() = runTest {
        val currency = Currency(symbol = "AA", "AA")
        currencyDao.insertCurrencies(listOf(currency))
        assertTrue(currencyDao.getCurrencyBySymbol("AA") != null)
    }

    @Test
    fun `Select not found currency should be null`() = runTest {
        val result = currencyDao.getCurrencyBySymbol("CC")
        assertNull(result, "result not null")
    }
}