package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseEmptyUseCase
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetExchangeRates(private val repo: OpenExchangeRepository) :
    BaseEmptyUseCase<List<Rate>> {

    override suspend fun invoke(): List<Rate> {
        return repo.getRates()
    }
}