package com.pratama.kmmcurrency.domain.usecase

import com.pratama.kmmcurrency.core.BaseEmptyUseCase
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetExchangeRates(private val repo: OpenExchangeRepository) :
    BaseEmptyUseCase<Result<List<Rate>>> {

    override suspend fun invoke(): Result<List<Rate>> {
        return repo.getRates()
    }
}