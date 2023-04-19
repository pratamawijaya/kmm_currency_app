package com.pratama.kmmcurrency.domain.usecase

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.DecimalFormat
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.core.measureTimeMillis
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import kotlinx.datetime.Clock

class CalculateExchangeRate(
    private val repo: OpenExchangeRepository,
    private val rateDao: RateDao,
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
        measureTimeMillis({ time -> Logger.i { "execution time calculate rate $time ms" } }, {
            val listExchangeRate = mutableListOf<ExchangeRate>()

            val rates = repo.getRates().getOrThrow()
            val fromToUSD = rateDao.getRateBySymbol(input.from).first().rate


            rates?.map {
                if (it.symbol != input.from) {
                    val rate = calculate(fromToUSD, it.symbol, input.amount)
                    listExchangeRate.add(ExchangeRate(it.symbol, rate))
                }
            }

            return Result.success(listExchangeRate)
        })
    }

    /**
     * 1 EUR * (1 USD / 0.903933 EUR/USD) * 14726.45 IDR/USD = 16282.10 IDR
     */
    private fun calculate(fromUSD: Double, target: String, amount: Double): Double {
        val targetToUSD = rateDao.getRateBySymbol(target).first().rate
        val rate = amount * (1 / fromUSD) * targetToUSD
        return decimalFormat.format(rate).toDouble()
    }


}

