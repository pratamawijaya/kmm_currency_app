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

    /**
     * # Replace YOUR_APP_ID with your API key
    response = requests.get("https://openexchangerates.org/api/latest.json?app_id=YOUR_APP_ID")

    data = response.json()

    # Get the exchange rate for USD to EUR
    usd_eur_rate = data["rates"]["EUR"]

    # Convert 100 USD to EUR
    usd_amount = 100
    eur_amount = usd_amount * usd_eur_rate

    print(f"{usd_amount} USD is {eur_amount} EUR")


    usd_amount = 1
    usd_eur_rate = 0.903933
    usd_idr_rate = 14726.45

    idr_eur_rate = usd_idr_rate / usd_eur_rate

    idr_amount = usd_amount * idr_eur_rate

    print(f"{usd_amount} USD is {idr_amount} IDR")


    1 EUR * (1 USD / EUR rate) * IDR rate = X IDR

    where X is the equivalent value of 1 EUR in IDR.

    Substituting the given exchange rates, we get:

    1 EUR * (1 USD / 0.903933 EUR/USD) * 14726.45 IDR/USD = 16282.10 IDR

     */

    override suspend fun invoke(input: Param): List<ExchangeRate> {
        val rates = repo.getRates()
        Logger.i { "get cached rates ${rates.size}" }
        val fromToUSD = rateDao.getRateBySymbol(input.from).rate
        val listExchangeRate = mutableListOf<ExchangeRate>()

        rates.map {
            if (it.symbol != input.from) {
                val targetToUSD = rateDao.getRateBySymbol(it.symbol).rate
                val rate = input.amount * (1 / fromToUSD) * targetToUSD
                val roundedAmount = decimalFormat.format(rate)
                Logger.i { "result rate ${input.from} - ${it.symbol} = $roundedAmount" }
                listExchangeRate.add(ExchangeRate(it.symbol, roundedAmount.toDouble()))
            }
        }

        return listExchangeRate

    }
}