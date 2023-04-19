package com.pratama.kmmcurrency

import io.ktor.client.*
import kotlinx.datetime.Clock
import org.koin.core.module.Module

interface Platform {
    val name: String
    fun getHttpClientEngine(forMultipartData: Boolean = false): HttpClient

    fun isDebugMode(): Boolean
}

interface AppInfo {
    val appId: String
}

expect fun getPlatform(): Platform

expect val platformModule: Module

expect interface DecimalFormat {
    fun format(value: Double): String
}
