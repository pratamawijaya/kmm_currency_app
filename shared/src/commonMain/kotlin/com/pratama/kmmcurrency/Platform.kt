package com.pratama.kmmcurrency

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform