package com.pratama.kmmcurrency.usecase

import com.pratama.kmmcurrency.FakeRepo
import com.pratama.kmmcurrency.domain.usecase.GetExchangeRates
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetExchangeRatesTest {

    private lateinit var getExchangeRatesTest: GetExchangeRates

    private val fakeRepo = FakeRepo()

    @BeforeTest
    fun setup() {
        getExchangeRatesTest = GetExchangeRates(fakeRepo)
    }

    @Test
    fun `test execute GetExchangeRateUseCase`() = runTest {
        val result = getExchangeRatesTest()
        assertTrue(result.getOrThrow().isNotEmpty(), "usecase result empty")
    }
}