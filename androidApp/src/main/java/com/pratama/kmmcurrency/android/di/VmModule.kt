package com.pratama.kmmcurrency.android.di

import com.pratama.kmmcurrency.android.presentation.OpenExchangeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val vmModule = module {
    viewModel {
        OpenExchangeViewModel(
            getCurrencies = get(),
            getExchangeRates = get(),
            calculateRates = get()
        )
    }
}