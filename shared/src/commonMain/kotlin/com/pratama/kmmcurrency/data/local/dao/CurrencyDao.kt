package com.pratama.kmmcurrency.data.local.dao

import com.pratama.kmmcurrency.domain.entity.Currency

interface CurrencyDao {
    fun clearDatabase()
    fun getCurrencies(): List<Currency>
    fun insertCurrencies(currencies: List<Currency>)

    fun getCurrencyBySymbol(symbol: String): Currency?
}