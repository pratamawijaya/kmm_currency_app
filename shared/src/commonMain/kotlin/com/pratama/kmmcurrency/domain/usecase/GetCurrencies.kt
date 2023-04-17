package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetCurrencies(private val repo: OpenExchangeRepository) :
    BaseUseCase<Boolean, List<Currency>> {

    override suspend fun invoke(shouldFetch: Boolean): List<Currency> {
        return repo.getCurrencies(shouldFetch = shouldFetch)
    }
}