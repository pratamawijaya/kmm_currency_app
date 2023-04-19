package com.pratama.kmmcurrency

import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.data.local.dao.FetcherDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import com.squareup.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver


class FakeFormatter : DecimalFormat {
    override fun format(value: Double): String {
        return "$value"
    }

}

class FakeRepo : OpenExchangeRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return Result.success(
            listOf(
                Currency("EUR", "Euro"),
                Currency("IDR", "Indonesia IDR")
            )
        )
    }

    override suspend fun getRates(): Result<List<Rate>> {
        return Result.success(baseRates)
    }

}

class FakeCurrencyDao : CurrencyDao {
    override fun clearDatabase() {

    }

    override fun getCurrencies(): List<Currency> {
        return listOf(Currency("IDR", "Indonesia"))
    }

    override fun insertCurrencies(currencies: List<Currency>) {
    }

    override fun getCurrencyBySymbol(symbol: String): Currency? {
        return Currency("IDR", "Indonesia")
    }

}

class FakeRateDao : RateDao {

    /**
     * rateDao.getRateBySymbol(target).first().rate
     */

    override suspend fun getRates(): List<Rate> {
        return baseRates
    }

    override fun insertRates(rates: List<Rate>) {
    }

    override fun getRateBySymbol(symbol: String): Rate? {
        return baseRates.find { it.symbol == symbol }
    }

}

internal val baseRates = listOf(
    Rate("EUR", 0.99),
    Rate("USD", 1.0),
    Rate("IDR", 14980.077709), // 1 EUR to IDR should be  16282.10
    Rate("AFN", 85.035284),
    Rate("ALL", 102.617804)
)

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