package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetExchangeRates(private val repo: OpenExchangeRepository) :
    BaseUseCase<GetExchangeRates.Param, List<Rate>> {

    data class Param(
        val shouldFetch: Boolean,
        val amount: Double,
        val symbol: String
    )

    override suspend fun invoke(input: Param): List<Rate> {
        return repo.getRates(
            shouldFetch = input.shouldFetch,
            symbol = input.symbol,
            amount = input.amount
        )
    }
}