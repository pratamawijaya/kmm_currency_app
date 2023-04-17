package com.pratama.kmmcurrency.android.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.pratama.kmmcurrency.android.R
import com.pratama.kmmcurrency.android.presentation.OpenExchangeViewModel.*
import com.pratama.kmmcurrency.android.presentation.adapter.ExchangeRateAdapter
import com.pratama.kmmcurrency.domain.entity.Currency
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenExchangeActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val openExchangeViewModel: OpenExchangeViewModel by viewModel()

    private var listCurrencies = mutableListOf<Currency>()
    lateinit var inputAmount: AppCompatEditText
    lateinit var rvExchangeRate: RecyclerView
    lateinit var exchangeRateAdapter: ExchangeRateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_exchange)

        val currencyDropDown = findViewById<AutoCompleteTextView>(R.id.currencyDropDown)
        inputAmount = findViewById(R.id.amount)
        rvExchangeRate = findViewById(R.id.rvExchangeRate)

        currencyDropDown.onItemClickListener = this

        openExchangeViewModel.uiState.observe(this) { state ->
            when (state) {
                is OpenExchangeState.GetCurrenciesSucces -> {
                    state.currencies.map {
                        listCurrencies.add(it)
                    }

                    currencyDropDown.setText(listCurrencies[0].symbol)

                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, state.currencies)

                    currencyDropDown.setAdapter(adapter)

                    openExchangeViewModel.getExchangeRate(
                        symbol = adapter.getItem(0)?.symbol ?: "",
                        amount = inputAmount.text.toString().toDouble()
                    )

                }

                is OpenExchangeState.SuccessCalculateRate -> {
                    exchangeRateAdapter = ExchangeRateAdapter(state.exchangRates)
                    rvExchangeRate.adapter = exchangeRateAdapter
                    val gridLayout = GridLayoutManager(this, 3)
                    rvExchangeRate.layoutManager = gridLayout
                }
                else -> {

                }
            }
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as Currency
        Log.d("debug", "selected item ${selectedItem.symbol}")
        openExchangeViewModel.getExchangeRate(
            selectedItem.symbol,
            amount = inputAmount.text.toString().toDouble()
        )
    }


}