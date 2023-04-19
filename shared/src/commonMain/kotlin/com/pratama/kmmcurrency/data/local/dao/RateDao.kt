package com.pratama.kmmcurrency.data.local.dao

import com.pratama.kmmcurrency.domain.entity.Rate

interface RateDao {
    suspend fun getRates(): List<Rate>
    fun insertRates(rates: List<Rate>)
    fun getRateBySymbol(symbol: String): List<Rate>
}