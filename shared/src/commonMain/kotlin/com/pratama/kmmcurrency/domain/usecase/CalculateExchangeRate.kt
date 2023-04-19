package com.pratama.kmmcurrency.domain.usecase

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.DecimalFormat
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.core.measureTimeMillis
import com.pratama.kmmcurrency.data.local.dao.CurrencyDao
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import kotlinx.datetime.Clock

class CalculateExchangeRate(
    private val repo: OpenExchangeRepository,
    private val rateDao: RateDao,
    private val currencyDao: CurrencyDao,
    private val decimalFormat: DecimalFormat
) :
    BaseUseCase<CalculateExchangeRate.Param, Result<List<ExchangeRate>>> {

    init {
        Logger.setTag("calculate_rate")
    }

    data class Param(
        val from: String,
        val amount: Double
    )

    override suspend fun invoke(input: Param): Result<List<ExchangeRate>> {
        val listExchangeRate = mutableListOf<ExchangeRate>()

        measureTimeMillis({ time -> Logger.i { "execution time calculate rate $time ms" } }, {
            val rates = repo.getRates().getOrThrow()
            val fromToUSD = rateDao.getRateBySymbol(input.from)?.rate ?: 0.0
            rates?.map {
                if (it.symbol != input.from) {
                    val rate = calculate(
                        fromUSD = fromToUSD,
                        target = it.symbol,
                        amount = input.amount
                    )
                    val name = currencyDao.getCurrencyBySymbol(it.symbol)?.name
                    listExchangeRate.add(
                        ExchangeRate(
                            symbol = it.symbol,
                            name = name ?: "",
                            rate = rate
                        )
                    )
                }
            }
        })

        return Result.success(listExchangeRate)
    }

    /**
     * 1 EUR * (1 USD / 0.903933 EUR/USD) * 14726.45 IDR/USD = 16282.10 IDR
     */
    private fun calculate(fromUSD: Double, target: String, amount: Double): Double {
        val targetToUSD = rateDao.getRateBySymbol(target)?.rate ?: 0.0
        val rate = amount * (1 / fromUSD) * targetToUSD
        return decimalFormat.format(rate).toDouble()
    }


}

