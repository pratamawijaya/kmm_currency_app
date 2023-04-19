package com.pratama.kmmcurrency

import com.pratama.kmmcurrency.data.local.dao.FetcherDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import com.pratama.kmmcurrency.usecase.baseRates
import com.squareup.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver


class FakeFormatter : DecimalFormat {
    override fun format(value: Double): String {
        return "$value"
    }

}

class FakeRepo : OpenExchangeRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return Result.success(listOf(Currency("EUR", "Euro")))
    }

    override suspend fun getRates(): Result<List<Rate>> {
        return Result.success(baseRates)
    }

}

class FakeRateDao : RateDao {


    override suspend fun getRates(): List<Rate> {
        return baseRates
    }

    override fun insertRates(rates: List<Rate>) {
    }

    override fun getRateBySymbol(symbol: String): Rate {
        return baseRates.find { it.symbol == symbol } ?: baseRates[0]
    }

}

class FakeFetcherDao : FetcherDao {
    var lastFetch: Long = 1L

    fun setupLastFetch(data: Long) {
        lastFetch = data
    }

    override fun insertLastFetch(key: String, timestamp: Long) {

    }


    override fun getLastFetch(key: String): Long {
        return lastFetch
    }

}