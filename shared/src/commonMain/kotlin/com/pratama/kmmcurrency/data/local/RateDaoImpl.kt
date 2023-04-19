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

    override suspend fun getRates(): List<Rate> {
        return queries.selectAllRate(::mapRate).executeAsList()
    }

    override fun insertRates(rates: List<Rate>) {
        Logger.d { "insert rate ${rates.size}" }
        queries.transaction {
            rates.map {
                queries.insertRate(it.symbol, it.rate)
            }
        }
    }

    override fun getRateBySymbol(symbol: String): List<Rate> {
        return queries.selectRateBySymbol(symbol, ::mapRate).executeAsList()
    }

    private fun mapRate(symbol: String, rate: Double): Rate {
        return Rate(symbol, rate)
    }

}