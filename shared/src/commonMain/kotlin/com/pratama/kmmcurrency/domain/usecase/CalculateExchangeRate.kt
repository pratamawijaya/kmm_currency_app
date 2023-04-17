package com.pratama.kmmcurrency.domain.usecase

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.DecimalFormat
import com.pratama.kmmcurrency.Platform
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.data.local.dao.RateDao
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class CalculateExchangeRate(
    private val repo: OpenExchangeRepository,
    private val rateDao: RateDao,
    private val decimalFormat: DecimalFormat
) :
    BaseUseCase<CalculateExchangeRate.Param, List<ExchangeRate>> {

    init {
        Logger.setTag("calculate_rate")
    }

    data class Param(
        val from: String,
        val amount: Double
    )

    override suspend fun invoke(input: Param): List<ExchangeRate> {
        val rates = repo.getRates()
        Logger.i { "get cached rates ${rates.size}" }
        val listExchangeRate = mutableListOf<ExchangeRate>()

        val fromToUSD = rateDao.getRateBySymbol(input.from).rate

        rates.map {
            if (it.symbol != input.from) {
                val rate = calculate(fromToUSD, it.symbol, input.amount)

                Logger.i { "result rate ${input.from} - ${it.symbol} = $rate" }

                listExchangeRate.add(ExchangeRate(it.symbol, rate))
            }
        }

        return listExchangeRate

    }

    private fun calculate(fromUSD: Double, target: String, amount: Double): Double {
        val targetToUSD = rateDao.getRateBySymbol(target).rate
        val rate = amount * (1 / fromUSD) * targetToUSD
        return decimalFormat.format(rate).toDouble()
    }
}