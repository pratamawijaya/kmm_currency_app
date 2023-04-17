package com.pratama.kmmcurrency.domain.entity

data class Currency(val symbol: String, val name: String) {
    override fun toString(): String {
        return symbol
    }
}