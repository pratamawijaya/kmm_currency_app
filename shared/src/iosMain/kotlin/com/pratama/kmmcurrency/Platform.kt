package com.pratama.kmmcurrency

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
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

actual fun getPlatform(): Platform = IOSPlatform()