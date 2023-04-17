package com.pratama.kmmcurrency.data.local

import com.pratama.kmmcurrency.cache.AppDatabaseQueries
import com.pratama.kmmcurrency.cache.BaseDao
import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.domain.entity.Currency

class CurrencyDaoImpl(private val database: CurrencyDatabase) : BaseDao(), CurrencyDao {

    override val queries: AppDatabaseQueries
        get() = database.appDatabaseQueries

    override fun clearDatabase() {
        queries.transaction {
            queries.removeAllCurrency()
        }
    }

    override fun getCurrencies(): List<Currency> {
        return queries.selectAllCurrency(::mapCurrency).executeAsList()
    }

    override fun insertCurrencies(currencies: List<Currency>) {
        queries.transaction {
            currencies.forEach { currency ->
                val data = queries.selectCurrencyBySymbol(currency.symbol).executeAsOneOrNull()
                if (data == null) {
                    queries.insertCurrency(currency.symbol, currency.name)
                }
            }
        }
    }

    override fun getCurrencyBySymbol(symbol: String): Currency? {
        return queries.selectCurrencyBySymbol(symbol, ::mapCurrency).executeAsOneOrNull()
    }


    private fun mapCurrency(symbol: String, name: String?): Currency {
        return Currency(symbol = symbol, name = name ?: "")
    }

}