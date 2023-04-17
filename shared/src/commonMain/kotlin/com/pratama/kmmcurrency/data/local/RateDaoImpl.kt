package com.pratama.kmmcurrency.data.local

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.cache.AppDatabaseQueries
import com.pratama.kmmcurrency.cache.BaseDao
import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.Rate

class RateDaoImpl(private val currencyDatabase: CurrencyDatabase) : RateDao, BaseDao() {

    init {
        Logger.setTag("pratama-debug-${this::class.simpleName}")
    }

    override val queries: AppDatabaseQueries
        get() = currencyDatabase.appDatabaseQueries

    override fun getRates(): List<Rate> {
        return queries.selectAllRate(::mapRate).executeAsList()
    }

    override fun insertRates(rates: List<Rate>) {
        queries.transaction {
            rates.map {
                Logger.d { "insert rate $it" }
                queries.insertRate(it.symbol, it.rate)
            }
        }
    }

    override fun getRateBySymbol(symbol: String): Rate {
        return queries.selectRateBySymbol(symbol, ::mapRate).executeAsOne()
    }

    private fun mapRate(symbol: String, rate: Double): Rate {
        return Rate(symbol, rate)
    }

}