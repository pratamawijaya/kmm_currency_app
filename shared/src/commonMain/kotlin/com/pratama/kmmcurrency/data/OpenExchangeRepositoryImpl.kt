package com.pratama.kmmcurrency.data

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.data.remote.OpenExchangeApi
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenExchangeRepositoryImpl(
    private val openExchangeApi: OpenExchangeApi,
    private val currencyDao: CurrencyDao,
    private val rateDao: RateDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : OpenExchangeRepository {

    override suspend fun getCurrencies(shouldFetch: Boolean): List<Currency> =
        withContext(dispatcher) {
            val cachedCurrency = currencyDao.getCurrencies()
            if (!shouldFetch && cachedCurrency.isNotEmpty()) {
                Logger.i { "there is cached currency" }
                cachedCurrency
            } else {
                Logger.i { "there are no-cached currency" }
                openExchangeApi.getCurrencies()
                    .also {
                        currencyDao.insertCurrencies(it)
                    }
            }

        }

    override suspend fun getRates(
        shouldFetch: Boolean,
        symbol: String,
        amount: Double
    ): List<Rate> = withContext(dispatcher) {
        val cachedRate = rateDao.getRates()

        if (cachedRate != null) {
            cachedRate
        } else {
            openExchangeApi.getRates().also {
                rateDao.insertRates(it)
            }
        }


    }
}