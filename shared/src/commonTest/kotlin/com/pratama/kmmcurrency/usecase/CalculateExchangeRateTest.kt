package com.pratama.kmmcurrency.usecase

import com.pratama.kmmcurrency.FakeCurrencyDao
import com.pratama.kmmcurrency.FakeFormatter
import com.pratama.kmmcurrency.FakeRateDao
import com.pratama.kmmcurrency.FakeRepo
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
    private val fakeCurrencyDao = FakeCurrencyDao()

    @BeforeTest
    fun setup() {

        calculateExchangeRate = CalculateExchangeRate(
            repo = fakeRepo,
            rateDao = fakeRateDao,
            decimalFormat = fakeFormatter,
            currencyDao = fakeCurrencyDao
        )
    }

    @Test
    fun `test calculate rate EURtoIDR`() = runTest {
        val expectedResult = 15131.391625252525
        val input = CalculateExchangeRate.Param("EUR", 1.0)
        val data = calculateExchangeRate.invoke(input).getOrThrow()

        val result = data.find { it.symbol == "IDR" }
        assertEquals(result?.rate, expectedResult)
    }

    @Test
    fun `test calculate rate EURtoALL`() = runTest {
        val expectedResult = 103.65434747474748
        val input = CalculateExchangeRate.Param("EUR", 1.0)
        val data = calculateExchangeRate.invoke(input).getOrThrow()

        val result = data.find { it.symbol == "ALL" }
        assertEquals(result?.rate, expectedResult)
    }

    @Test
    fun `test calculate rate EURtoIDR amount10`() = runTest {
        val expectedResult = 151313.91625252526
        val input = CalculateExchangeRate.Param("EUR", 10.0)
        val data = calculateExchangeRate.invoke(input).getOrThrow()

        val result = data.find { it.symbol == "IDR" }
        assertEquals(result?.rate, expectedResult)

    }
}



