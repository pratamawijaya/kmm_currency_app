package com.pratama.kmmcurrency.di

import com.pratama.kmmcurrency.data.OpenExchangeRepositoryImpl
import com.pratama.kmmcurrency.data.local.CurrencyDaoImpl
import com.pratama.kmmcurrency.data.local.RateDaoImpl
import com.pratama.kmmcurrency.data.remote.OpenExchangeApi
import com.pratama.kmmcurrency.data.remote.OpenExchangeApiImpl
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository
import com.pratama.kmmcurrency.domain.usecase.GetCurrencies
import com.pratama.kmmcurrency.domain.usecase.GetExchangeRates
import com.pratama.kmmcurrency.getPlatform
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val openExchangeModule = module {
    //dao
    single {
        CurrencyDaoImpl(get())
    }
    single {
        RateDaoImpl(get())
    }
    // api
    single<OpenExchangeApi> {
        OpenExchangeApiImpl(
            httpClient = setupHttpClient(
                isDebugMode = getPlatform().isDebugMode()
            ),
            appId = "fe74756e79314f289bc0ca2b27f93aa3"
        )
    }

    // repo
    single<OpenExchangeRepository> {
        OpenExchangeRepositoryImpl(
            openExchangeApi = get(),
            get(),
            get()
        )
    }

    // usecase
    single { GetCurrencies(repo = get()) }
    single { GetExchangeRates(repo = get()) }
}

fun setupHttpClient(
    isDebugMode: Boolean = false
) = HttpClient {

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
        })
    }

    install(HttpTimeout) {
        val timeout = 30_000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }

    if (isDebugMode) {
        install(Logging) {
            logger = CustomLogger()
            level = LogLevel.ALL
        }
    }

}

class CustomLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.withTag("http_log").d { message }
    }

}
