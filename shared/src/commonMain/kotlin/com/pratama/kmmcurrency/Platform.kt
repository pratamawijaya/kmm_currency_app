package com.pratama.kmmcurrency

import io.ktor.client.*
import org.koin.core.module.Module

interface Platform {
    val name: String
    fun getHttpClientEngine(forMultipartData: Boolean = false): HttpClient

    fun isDebugMode(): Boolean
}

expect fun getPlatform(): Platform

expect val platformModule: Module