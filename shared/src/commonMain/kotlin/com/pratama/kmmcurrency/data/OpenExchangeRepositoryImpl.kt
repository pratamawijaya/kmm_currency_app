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
                        it.getOrNull()?.let { listCurrencies ->
                            currencyDao.insertCurrencies(listCurrencies)
                        }
                    }
            }
        }

    override suspend fun getRates(
    ): List<Rate> = withContext(dispatcher) {
        Logger.i { "get exchange rates" }
        val cachedRate = rateDao.getRates()
        Logger.i { "cached rate -> ${cachedRate.size}" }

        cachedRate.ifEmpty {
            openExchangeApi.getRates().also {
                rateDao.insertRates(it)
            }
        }
    }

    override suspend fun calculateExchangeRate(
        from: String,
        amount: Double
    ): List<ExchangeRate> {

        val cachedRates = rateDao.getRates()
        val fromUsdRate = rateDao.getRateBySymbol(from).rate

        val listExchangeRate = mutableListOf<ExchangeRate>()

        Logger.i { "get cached rates ${cachedRates.size}" }

        cachedRates.map {
            if (it.symbol != from) {
                val getToUsdRate = rateDao.getRateBySymbol(it.symbol).rate
                val rateResult = fromUsdRate / getToUsdRate
                Logger.i { "calculate rate from $from -> ${it.symbol} : $rateResult" }
                listExchangeRate.add(ExchangeRate(it.symbol, rateResult))
            }
        }

        return listExchangeRate
    }

}