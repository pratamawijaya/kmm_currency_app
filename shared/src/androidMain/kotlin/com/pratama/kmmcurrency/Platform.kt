package com.pratama.kmmcurrency

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidPlatform : Platform, KoinComponent {
    private val context: Context by inject()

    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override fun getHttpClientEngine(forMultipartData: Boolean): HttpClient {
        return if (forMultipartData) HttpClient(CIO) else HttpClient(OkHttp) {
            engine {
                config {
                    retryOnConnectionFailure(true)
                    followRedirects(true)
                }
                if (isDebugMode()) {
                    addInterceptor(ChuckerInterceptor.Builder(context).build())
                }
            }
        }
    }

    override fun isDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual class DecimalFormat actual constructor() {
    actual fun format(value: Double): String {
        val df = java.text.DecimalFormat()
        df.isGroupingUsed = false
        df.maximumFractionDigits = 2
        df.isDecimalSeparatorAlwaysShown = false
        return df.format(value)
    }
}