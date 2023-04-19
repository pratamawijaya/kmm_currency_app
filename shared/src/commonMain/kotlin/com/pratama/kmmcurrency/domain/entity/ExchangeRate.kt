package com.pratama.kmmcurrency.domain.entity

data class ExchangeRate(
    val symbol: String,
    val name: String,
    val value: Double // 10.000
)
