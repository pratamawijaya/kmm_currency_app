package com.pratama.kmmcurrency.domain.entity

data class ExchangeRate(
    val symbol: String,
    val name: String,
    val rate: Double // 10.000
)
