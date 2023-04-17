package com.pratama.kmmcurrency.domain.repository

import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.entity.Rate

interface OpenExchangeRepository {
    suspend fun getCurrencies(shouldFetch: Boolean = false): Result<List<Currency>>
    suspend fun getRates(): List<Rate>

    suspend fun calculateExchangeRate(from: String, amount: Double): List<ExchangeRate>
}