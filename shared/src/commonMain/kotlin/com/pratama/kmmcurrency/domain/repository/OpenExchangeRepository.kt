package com.pratama.kmmcurrency.domain.repository

import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.entity.Rate

interface OpenExchangeRepository {
    suspend fun getCurrencies(): Result<List<Currency>>
    suspend fun getRates(): Result<List<Rate>>

}