package com.pratama.kmmcurrency.core.network

data class HttpException(val httpCode: Int, val msg: String?) : Throwable()
object NetworkException : Throwable()
object UnknownException : Throwable()