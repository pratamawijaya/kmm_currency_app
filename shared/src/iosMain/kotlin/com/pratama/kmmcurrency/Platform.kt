package com.pratama.kmmcurrency

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override fun getHttpClientEngine(forMultipartData: Boolean): HttpClient {
        return HttpClient(Darwin) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
        }
    }

    override fun isDebugMode(): Boolean {
        return true
    }
}

actual interface DecimalFormat {
    actual fun format(value: Double): String
}

class DecimalFormatImplIOS : DecimalFormat {
    override fun format(value: Double): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 2u
        formatter.numberStyle = 1u
        return formatter.stringFromNumber(NSNumber(value))!!
    }

}

actual fun getPlatform(): Platform = IOSPlatform()