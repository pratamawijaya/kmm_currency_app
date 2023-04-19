package com.pratama.kmmcurrency.android.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pratama.kmmcurrency.android.R
import com.pratama.kmmcurrency.android.databinding.ItemExchangeRateeBinding
import com.pratama.kmmcurrency.domain.entity.ExchangeRate

fun Double.round(decimalPrecision: Int): Double {
    return "%.0${decimalPrecision}f".format(this).toDouble()
}

class ExchangeRateAdapter(
    private val exchangeRates: List<ExchangeRate>,
    val amount: Double,
    val labelFrom: String
) :
    RecyclerView.Adapter<ExchangeRateAdapter.ExchangeRateViewHolder>() {

    inner class ExchangeRateViewHolder(private val binding: ItemExchangeRateeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(exchangeRate: ExchangeRate) {
            binding.tvLabelTo.text = exchangeRate.symbol
            binding.tvLabelToName.text = exchangeRate.name
            binding.tvLabelValue.text = "${exchangeRate.value}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val binding = ItemExchangeRateeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeRateViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exchangeRates.size
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bindView(exchangeRates[position])
    }

}