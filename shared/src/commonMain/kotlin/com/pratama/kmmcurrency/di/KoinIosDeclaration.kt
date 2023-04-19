package com.pratama.kmmcurrency.di

import com.pratama.kmmcurrency.core.CoreApp
import com.pratama.kmmcurrency.domain.usecase.CalculateExchangeRate
import com.pratama.kmmcurrency.domain.usecase.GetCurrencies
import com.pratama.kmmcurrency.domain.usecase.GetExchangeRates
import org.koin.core.Koin
import org.koin.core.KoinApplication

fun KoinApplication.Companion.start(): KoinApplication = CoreApp.initialize {}

val Koin.calculateExchangeRateUseCase: CalculateExchangeRate
    get() = get()
val Koin.getCurrenciesUseCase: GetCurrencies
    get() = get()
val Koin.getExchangeRates: GetExchangeRates
    get() = get()