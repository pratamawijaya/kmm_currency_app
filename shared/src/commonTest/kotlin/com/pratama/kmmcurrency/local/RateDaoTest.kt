package com.pratama.kmmcurrency.local

import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.RateDaoImpl
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.testDbConnection
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class RateDaoTest {

    private lateinit var currencyDb: CurrencyDatabase
    private lateinit var rateDao: RateDao

    @BeforeTest
    fun setup() = runTest {
        currencyDb = CurrencyDatabase(testDbConnection())
        rateDao = RateDaoImpl(currencyDb)
    }

    @Test
    fun `insert rate should be success`() = runTest {
        val rate = Rate(symbol = "USD", 0.1)

        rateDao.insertRates(listOf(rate))

        assertNotNull(rateDao.getRateBySymbol("USD"))
    }

    @Test
    fun `get rates should be success`() = runTest {
        val rates = rateDao.getRates()
        assertNotNull(rates, "could not retreive rates")
    }
}