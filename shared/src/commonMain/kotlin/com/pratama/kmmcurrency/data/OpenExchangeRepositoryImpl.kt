package com.pratama.kmmcurrency.data

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.data.local.dao.FetcherDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.data.remote.OpenExchangeApi
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class OpenExchangeRepositoryImpl(
    private val openExchangeApi: OpenExchangeApi,
    private val currencyDao: CurrencyDao,
    private val rateDao: RateDao,
    private val fetchDao: FetcherDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
) : OpenExchangeRepository {

    companion object {
        const val fetchKeyRate = "rates"
        const val fetchKeyCurrency = "currency"
        private const val THRESHOLD = 1800 // 30 minute
    }

    init {
        Logger.setTag("pratama-debug")
    }

    override suspend fun getCurrencies(): Result<List<Currency>> =
        withContext(dispatcher) {
            val cachedCurrency = currencyDao.getCurrencies()
            val isMoreThan30Minutes =
                canFetch(fetchKeyCurrency, Clock.System.now().epochSeconds)

            Logger.d { "cachedCurrency -> ${cachedCurrency.size}" }
            Logger.d { "should fetch ? : $isMoreThan30Minutes" }


            if (!isMoreThan30Minutes && cachedCurrency.isNotEmpty()) {
                Logger.i { "there is cached currency" }
                Result.success(cachedCurrency)
            } else {
                Logger.i { "there are no-cached currency" }
                openExchangeApi.getCurrencies()
                    .also {
                        fetchDao.insertLastFetch(fetchKeyCurrency, Clock.System.now().epochSeconds)
                        currencyDao.insertCurrencies(it.getOrDefault(emptyList()))
                    }
            }
        }

    override suspend fun getRates(): Result<List<Rate>> = withContext(dispatcher) {
        Logger.i { "get exchange rates" }
        val cachedRate = rateDao.getRates()
        val isMoreThan30minutes = canFetch(fetchKeyRate, Clock.System.now().epochSeconds)

        Logger.i { "cached rate is not empty : ${cachedRate.isNotEmpty()}" }
        Logger.i { "cached rate more than 30minutes : ${!isMoreThan30minutes}" }

        if (cachedRate.isNotEmpty() && !isMoreThan30minutes) {
            Result.success(cachedRate)
        } else {
            val result = openExchangeApi.getRates()
            result.getOrNull()?.also {
                rateDao.insertRates(it)
                fetchDao.insertLastFetch(fetchKeyRate, Clock.System.now().epochSeconds)
            }
            return@withContext result
        }
    }

    private fun canFetch(key: String, currentTimestamp: Long): Boolean {
        val lastFetch = fetchDao.getLastFetch(key) ?: return true
        return (currentTimestamp - lastFetch) < THRESHOLD
    }

}


