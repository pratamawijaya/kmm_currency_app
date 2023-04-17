package com.pratama.kmmcurrency.data.remote

import com.pratama.kmmcurrency.core.network.ApiResponse
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate

interface OpenExchangeApi {
    suspend fun getCurrencies(): Result<List<Currency>>

    suspend fun getRates(): List<Rate>
}