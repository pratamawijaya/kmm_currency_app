package com.pratama.kmmcurrency.data.remote

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.core.network.*
import com.pratama.kmmcurrency.data.remote.response.RateResponse
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder

class OpenExchangeApiImpl(
    private val httpClient: HttpClient,
    private val appId: String
) : OpenExchangeApi {

    init {
        Logger.setTag("pratama-debug-${this::class.simpleName}")
    }

    override suspend fun getCurrencies(): Result<List<Currency>> {
        Logger.d { "trying get currency" }
        val response = httpClient.safeRequest<Map<String, String>, String> {
            url("https://openexchangerates.org/api/currencies.json")
            method = HttpMethod.Get
        }

        return when (response) {
            is ApiResponse.Success -> {
                Logger.d { "get currencies success" }
                val result = response.body
                val listCurrency = mutableListOf<Currency>()
                result.map {
                    listCurrency.add(Currency(it.key, it.value))
                }
                Result.success(listCurrency)
            }

            is ApiResponse.Error.HttpError -> {
                Logger.e { "get currencies error ${response.code} ${response.errorBody}" }
                return Result.failure(HttpException(response.code, msg = response.errorBody))
            }
            is ApiResponse.Error.NetworkError -> {
                Logger.e { "get currencies network error " }
                return Result.failure(NetworkException)
            }
            else -> {
                return Result.failure(UnknownException)
            }
        }
    }

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