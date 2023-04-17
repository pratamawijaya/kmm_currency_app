package com.pratama.kmmcurrency.android.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.usecase.GetCurrencies
import com.pratama.kmmcurrency.domain.usecase.GetExchangeRates
import kotlinx.coroutines.launch

class OpenExchangeViewModel(
    private val getCurrencies: GetCurrencies,
    private val getExchangeRates: GetExchangeRates
) : ViewModel() {

    sealed class OpenExchangeState {
        data class GetCurrenciesSucces(val currencies: List<Currency>) : OpenExchangeState()
    }

    private val _uiState: MutableLiveData<OpenExchangeState> = MutableLiveData()
    val uiState = _uiState

    init {
        viewModelScope.launch {
            val data = getCurrencies.invoke(shouldFetch = true)
            _uiState.postValue(OpenExchangeState.GetCurrenciesSucces(data))
        }
    }

    fun getExchangeRate(symbol: String, amount: Double) {
        viewModelScope.launch {
            val param = GetExchangeRates.Param(
                shouldFetch = true,
                symbol = symbol,
                amount = amount
            )
            val data = getExchangeRates.invoke(param)
        }
    }
}