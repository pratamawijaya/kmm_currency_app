package com.pratama.kmmcurrency.usecase

import com.pratama.kmmcurrency.DecimalFormat
import com.pratama.kmmcurrency.FakeFormatter
import com.pratama.kmmcurrency.FakeRateDao
import com.pratama.kmmcurrency.FakeRepo
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import com.pratama.kmmcurrency.domain.usecase.CalculateExchangeRate
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateExchangeRateTest {

    private lateinit var calculateExchangeRate: CalculateExchangeRate
    private val fakeRepo = FakeRepo()
    private val fakeRateDao = FakeRateDao()
    private val fakeFormatter = FakeFormatter()

    @BeforeTest
    fun setup() {

        calculateExchangeRate = CalculateExchangeRate(
            repo = fakeRepo,
            rateDao = fakeRateDao,
            decimalFormat = fakeFormatter
        )
    }

    /**
     *
     * 1 EUR * (1 USD / 1 EUR/USD) * 2 IDR/USD = 2 IDR
     */
    @Test
    fun `test calculate rate EURtoIDR`() = runTest {
        val expectedResult = 2.0
        val input = CalculateExchangeRate.Param("EUR", 1.0)
        val result = calculateExchangeRate.invoke(input)

        assertEquals(result[0].symbol, "IDR")
        assertEquals(result[0].value, expectedResult)
    }

    /**
     *
     * 1 EUR * (1 USD / 1 EUR/USD) * 1.5 ALL/USD = 1.5 ALL
     */
    @Test
    fun `test calculate rate EURtoALL`() = runTest {
        val expectedResult = 1.5
        val input = CalculateExchangeRate.Param("EUR", 1.0)
        val result = calculateExchangeRate.invoke(input)

        assertEquals(result[2].symbol, "ALL")
        assertEquals(result[2].value, expectedResult)
    }

    @Test
    fun `test calculate rate EURtoIDR amount10`() = runTest {
        val expectedResult = 20.0
        val input = CalculateExchangeRate.Param("EUR", 10.0)
        val result = calculateExchangeRate.invoke(input)

        assertEquals(result[0].symbol, "IDR")
        assertEquals(result[0].value, expectedResult)
    }
}

internal val baseRates = listOf(
    Rate("EUR", 1.0),
    Rate("IDR", 2.0), // 1 EUR to IDR should be  16282.10
    Rate("AED", 5.0), // 1 EUR to AED should be 4.03
    Rate("ALL", 1.5)
)

