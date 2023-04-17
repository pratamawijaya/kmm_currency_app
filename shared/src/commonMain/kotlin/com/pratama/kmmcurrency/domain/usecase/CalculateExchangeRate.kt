package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.ExchangeRate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class CalculateExchangeRate(private val repo: OpenExchangeRepository) :
    BaseUseCase<CalculateExchangeRate.Param, List<ExchangeRate>> {

    data class Param(
        val from: String,
        val amount: Double
    )

    override suspend fun invoke(input: Param): List<ExchangeRate> {
        return repo.calculateExchangeRate(
            input.from,
            input.amount
        )
    }
}