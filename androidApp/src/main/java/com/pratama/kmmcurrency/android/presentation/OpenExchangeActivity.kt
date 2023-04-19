package com.pratama.kmmcurrency.android.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.pratama.kmmcurrency.android.R
import com.pratama.kmmcurrency.android.core.BaseActivityBinding
import com.pratama.kmmcurrency.android.databinding.ActivityOpenExchangeBinding
import com.pratama.kmmcurrency.android.presentation.OpenExchangeViewModel.*
import com.pratama.kmmcurrency.android.presentation.adapter.ExchangeRateAdapter
import com.pratama.kmmcurrency.domain.entity.Currency
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenExchangeActivity : BaseActivityBinding<ActivityOpenExchangeBinding>(),
    AdapterView.OnItemClickListener {

    private val openExchangeViewModel: OpenExchangeViewModel by viewModel()

    private var listCurrencies = mutableListOf<Currency>()
    lateinit var exchangeRateAdapter: ExchangeRateAdapter
    private var selectedItem: Currency? = null

    override val bindingInflater: (LayoutInflater) -> ActivityOpenExchangeBinding
        get() = ActivityOpenExchangeBinding::inflate

    override fun setupView(binding: ActivityOpenExchangeBinding) {

        with(binding) {
            currencyDropDown.onItemClickListener = this@OpenExchangeActivity

            textInputAmount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    selectedItem?.let {
                        Log.d("debug", "on text changed ${it.symbol}")
                        if (s.isNotEmpty()) {
                            openExchangeViewModel.getExchangeRate(
                                it.symbol,
                                amount = s.toString().toDouble()
                            )
                        }

                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            openExchangeViewModel.uiState.observe(this@OpenExchangeActivity) { state ->
                when (state) {
                    is OpenExchangeState.Loading -> {
                        loadingProgress.visibility = View.VISIBLE
                        rvExchangeRate.visibility = View.GONE

                    }
                    is OpenExchangeState.GetCurrenciesSucces -> {

                        loadingProgress.visibility = View.GONE
                        rvExchangeRate.visibility = View.VISIBLE

                        state.currencies.map {
                            listCurrencies.add(it)
                        }

                        currencyDropDown.setText(listCurrencies[0].symbol)

                        val adapter =
                            ArrayAdapter(
                                this@OpenExchangeActivity,
                                android.R.layout.simple_list_item_1,
                                state.currencies
                            )

                        currencyDropDown.setAdapter(adapter)

                        openExchangeViewModel.getExchangeRate(
                            symbol = adapter.getItem(0)?.symbol ?: "",
                            amount = textInputAmount.text.toString().toDouble()
                        )

                        selectedItem = adapter.getItem(0)

                    }

                    is OpenExchangeState.SuccessCalculateRate -> {
                        exchangeRateAdapter = ExchangeRateAdapter(
                            state.exchangRates,
                            amount = textInputAmount.text.toString().toDouble(),
                            labelFrom = selectedItem?.symbol ?: ""
                        )
                        rvExchangeRate.adapter = exchangeRateAdapter
                        rvExchangeRate.layoutManager =
                            LinearLayoutManager(this@OpenExchangeActivity)
                    }
                    else -> {

                    }
                }
            }
        }
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedItem = parent?.getItemAtPosition(position) as Currency
        selectedItem?.let {
            Log.d("debug", "selected item ${it.symbol}")
            openExchangeViewModel.getExchangeRate(
                it.symbol,
                amount = binding.textInputAmount.text.toString().toDouble()
            )
        }

    }


}