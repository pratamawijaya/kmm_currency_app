package com.pratama.kmmcurrency.android.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pratama.kmmcurrency.android.R
import com.pratama.kmmcurrency.domain.entity.ExchangeRate

fun Double.round(decimalPrecision: Int): Double {
    return "%.0${decimalPrecision}f".format(this).toDouble()
}

class ExchangeRateAdapter(private val exchangeRates: List<ExchangeRate>) :
    RecyclerView.Adapter<ExchangeRateAdapter.ExchangeRateViewHolder>() {

    inner class ExchangeRateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(exchangeRate: ExchangeRate) {
            val tvRate = view.findViewById<TextView>(R.id.tvRate)
            val tvSymbol = view.findViewById<TextView>(R.id.tvSymbol)

            tvRate.text = "${exchangeRate.value}"
            tvSymbol.text = exchangeRate.symbol
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        return ExchangeRateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_exchange_rate, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return exchangeRates.size
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bindView(exchangeRates[position])
    }

}