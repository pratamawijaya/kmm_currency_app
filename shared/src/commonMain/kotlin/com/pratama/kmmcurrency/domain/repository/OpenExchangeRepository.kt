package com.pratama.kmmcurrency.domain.repository

import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate

interface OpenExchangeRepository {
    suspend fun getCurrencies(shouldFetch: Boolean = false): List<Currency>
    suspend fun getRates(shouldFetch: Boolean = false, symbol: String, amount: Double): List<Rate>
}