package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseEmptyUseCase
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetCurrencies(private val repo: OpenExchangeRepository) :
    BaseEmptyUseCase<Result<List<Currency>>> {

    override suspend fun invoke(): Result<List<Currency>> {
        return repo.getCurrencies()
    }
}