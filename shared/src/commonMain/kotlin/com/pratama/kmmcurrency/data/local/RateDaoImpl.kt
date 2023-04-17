package com.pratama.kmmcurrency.data.local

import com.pratama.kmmcurrency.cache.AppDatabaseQueries
import com.pratama.kmmcurrency.cache.BaseDao
import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Rate

class RateDaoImpl(private val currencyDatabase: CurrencyDatabase) : RateDao, BaseDao() {

    override val queries: AppDatabaseQueries
        get() = currencyDatabase.appDatabaseQueries

    override fun getRates(): List<Rate> {
        return queries.selectAllRate(::mapRate).executeAsList()
    }

    override fun insertRates(rates: List<Rate>) {
        queries.transaction {
            rates.map {
                queries.insertRate(it.symbol, it.rate)
            }
        }
    }

    override fun getRateBySymbol(symbol: String): Rate? {
        return queries.selectRateBySymbol(symbol, ::mapRate).executeAsOneOrNull()
    }

    private fun mapRate(symbol: String, rate: Double): Rate {
        return Rate(symbol, rate)
    }

}