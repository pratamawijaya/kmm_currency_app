package com.pratama.kmmcurrency.data.remote

import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate

interface OpenExchangeApi {
    suspend fun getCurrencies(): List<Currency>

    suspend fun getRates(): List<Rate>
}