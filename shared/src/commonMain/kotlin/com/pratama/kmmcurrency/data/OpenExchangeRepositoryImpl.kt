package com.pratama.kmmcurrency.data

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.data.remote.OpenExchangeApi
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class OpenExchangeRepositoryImpl(
    private val openExchangeApi: OpenExchangeApi,
    private val currencyDao: CurrencyDao,
    private val rateDao: RateDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : OpenExchangeRepository {

    init {
        Logger.setTag("pratama-debug")
    }

    override suspend fun getCurrencies(shouldFetch: Boolean): Result<List<Currency>> =
        withContext(dispatcher) {
            val cachedCurrency = currencyDao.getCurrencies()
            Logger.d { "cachedCurrency -> ${cachedCurrency.size}" }
            Logger.d { "should fetch ? : $shouldFetch" }


            if (!shouldFetch && cachedCurrency.isNotEmpty()) {
                Logger.i { "there is cached currency" }
                Result.success(cachedCurrency)
            } else {
                Logger.i { "there are no-cached currency" }
                openExchangeApi.getCurrencies()
                    .also {
                        currencyDao.insertCurrencies(it.getOrDefault(emptyList()))
                    }
            }
        }

    override suspend fun getRates(): Result<List<Rate>> = withContext(dispatcher) {
        Logger.i { "get exchange rates" }
        val cachedRate = rateDao.getRates()

        Logger.i { "cached rate -> ${cachedRate.size}" }
        if (cachedRate != null) {
            Result.success(cachedRate)
        } else {
            openExchangeApi.getRates().also {
                rateDao.insertRates(it.getOrDefault(emptyList()))
            }
        }
    }
}