package com.pratama.kmmcurrency.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RateResponse(
    @SerialName("rates")
    val rates: Map<String, Double>,
    val disclaimer: String?,
    val license: String?,
    val timestamp: Long?,
    val base: String?
)