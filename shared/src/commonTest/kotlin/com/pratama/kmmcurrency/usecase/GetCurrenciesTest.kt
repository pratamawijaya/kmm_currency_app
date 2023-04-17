package com.pratama.kmmcurrency.usecase

import com.pratama.kmmcurrency.FakeRepo
import com.pratama.kmmcurrency.domain.usecase.GetCurrencies
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetCurrenciesTest {

    private lateinit var getCurrenciesTest: GetCurrencies

    private val fakeRepo = FakeRepo()

    @BeforeTest
    fun setup() {
        getCurrenciesTest = GetCurrencies(fakeRepo)
    }

    @Test
    fun `test execute GetCurrenciesUseCase`() = runTest {
        val result = getCurrenciesTest()
        assertTrue(result.getOrThrow().isNotEmpty(), "usecase result empty")
    }

}