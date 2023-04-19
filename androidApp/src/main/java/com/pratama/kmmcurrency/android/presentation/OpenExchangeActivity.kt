package com.pratama.kmmcurrency.android.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    lateinit var progresssBar: ProgressBar

    lateinit var exchangeRateAdapter: ExchangeRateAdapter
    private var selectedItem: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_exchange)

        val currencyDropDown = findViewById<AutoCompleteTextView>(R.id.currencyDropDown)
        inputAmount = findViewById(R.id.amount)
        rvExchangeRate = findViewById(R.id.rvExchangeRate)
        progresssBar = findViewById(R.id.loadingProgress)

        currencyDropDown.onItemClickListener = this


        inputAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
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

        openExchangeViewModel.uiState.observe(this) { state ->
            when (state) {
                is OpenExchangeState.Loading -> {
                    progresssBar.visibility = View.VISIBLE
                    rvExchangeRate.visibility = View.GONE

                }
                is OpenExchangeState.GetCurrenciesSucces -> {

                    progresssBar.visibility = View.GONE
                    rvExchangeRate.visibility = View.VISIBLE

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

                    selectedItem = adapter.getItem(0)

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
        selectedItem = parent?.getItemAtPosition(position) as Currency
        selectedItem?.let {
            Log.d("debug", "selected item ${it.symbol}")
            openExchangeViewModel.getExchangeRate(
                it.symbol,
                amount = inputAmount.text.toString().toDouble()
            )
        }

    }


}