package com.pratama.kmmcurrency.data.remote

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.data.remote.response.RateResponse
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder

class OpenExchangeApiImpl(
    private val httpClient: HttpClient,
    private val appId: String
) : OpenExchangeApi {

    override suspend fun getCurrencies(): List<Currency> {
        val response = httpClient.get("https://openexchangerates.org/api/currencies.json")
            .body<Map<String, String>>()
        val listCurrency = mutableListOf<Currency>()
        response.map {
            listCurrency.add(Currency(it.key, it.value))
        }
        return listCurrency
    }

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
     */
    override suspend fun getRates(): List<Rate> {
        val response =
            httpClient.get("https://openexchangerates.org/api/latest.json?app_id=$appId")
                .body<String>()

        val exchangeRates = Json.decodeFromString<RateResponse>(response)
        val rates = exchangeRates.rates
        val timestamp = exchangeRates.timestamp

        val listRate = mutableListOf<Rate>()

        rates.map {
            Logger.i { "rates ${it.key} ${it.value} -- $timestamp" }
            listRate.add(Rate(it.key, it.value))
        }
        return listRate
    }
}