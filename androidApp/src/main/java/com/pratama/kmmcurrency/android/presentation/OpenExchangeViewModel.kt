package com.pratama.kmmcurrency.android.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.usecase.CalculateExchangeRate
import com.pratama.kmmcurrency.domain.usecase.GetCurrencies
import com.pratama.kmmcurrency.domain.usecase.GetExchangeRates
import kotlinx.coroutines.launch

class OpenExchangeViewModel(
    private val getCurrencies: GetCurrencies,
    private val getExchangeRates: GetExchangeRates,
    private val calculateRates: CalculateExchangeRate
) : ViewModel() {

    sealed class OpenExchangeState {
        data class GetCurrenciesSucces(val currencies: List<Currency>) : OpenExchangeState()
        data class SuccessCalculateRate(val exchangRates: List<ExchangeRate>) : OpenExchangeState()

        object Loading : OpenExchangeState()
        object Error : OpenExchangeState()
    }

    private val _uiState: MutableLiveData<OpenExchangeState> = MutableLiveData()
    val uiState = _uiState

    init {
        viewModelScope.launch {
            _uiState.postValue(OpenExchangeState.Loading)
            val data = getCurrencies.invoke()
            getExchangeRates.invoke()

            if (data.isSuccess) {
                data.getOrNull()?.let {
                    _uiState.postValue(OpenExchangeState.GetCurrenciesSucces(it))
                }
            } else {
                // failed
                _uiState.postValue(OpenExchangeState.Error)
            }
        }
    }

    fun getExchangeRate(symbol: String, amount: Double) {
        viewModelScope.launch {
            if (amount > 0) {
                val param = CalculateExchangeRate.Param(
                    from = symbol,
                    amount = amount
                )
                val result = calculateRates.invoke(param)

                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        _uiState.postValue(OpenExchangeState.SuccessCalculateRate(it))
                    }
                } else {

                }


            } else {
                // amount 0
            }


        }
    }
}