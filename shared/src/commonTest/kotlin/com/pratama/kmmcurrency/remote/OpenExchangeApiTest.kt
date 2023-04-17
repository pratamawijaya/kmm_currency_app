package com.pratama.kmmcurrency.remote

import com.pratama.kmmcurrency.data.remote.OpenExchangeApiImpl
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.Rate
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.*

class OpenExchangeApiTest {

    @Test
    fun testRequestCurrencyShouldBeSuccess() = runTest {
        val engine = MockEngine {
            assertEquals(
                "https://openexchangerates.org/api/currencies.json",
                it.url.toString()
            )
            respond(
                content = """
                            {
                              "AED": "United Arab Emirates Dirham",
                              "AFN": "Afghan Afghani",
                              "ALL": "Albanian Lek"
                            }
                            """,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val openExchangeApi = OpenExchangeApiImpl(getMockClient(engine), "")
        val result = openExchangeApi.getCurrencies()
        assertEquals(
            listOf(
                Currency(
                    symbol = "AED",
                    name = "United Arab Emirates Dirham"
                ),
                Currency(
                    symbol = "AFN",
                    name = "Afghan Afghani"
                ),
                Currency(
                    symbol = "ALL",
                    name = "Albanian Lek"
                )
            ), result
        )

    }

    @Test
    fun testRequestRatesShouldBeSuccess() = runTest {
        val engine = MockEngine {
            assertEquals(
                "https://openexchangerates.org/api/latest.json?app_id=aoe",
                it.url.toString()
            )
            respond(
                content = """
                    {
                        "disclaimer": "Usage subject to terms: https://openexchangerates.org/terms",
                        "license": "https://openexchangerates.org/license",
                        "timestamp": 1681369200,
                        "base": "USD",
                        "rates": {
                            "AED": 3.6724,
                            "AFN": 86.859802
                        }
                    }
                    
                """.trimIndent(),
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val openExchangeApi = OpenExchangeApiImpl(getMockClient(engine), "aoe")
        val result = openExchangeApi.getRates()
        assertEquals(
            listOf(
                Rate(symbol = "AED", rate = 3.6724),
                Rate(symbol = "AFN", rate = 86.859802),
            ), result
        )
    }

    private fun getMockClient(engine: MockEngine): HttpClient {
        val mockClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
        }
        return mockClient
    }
}