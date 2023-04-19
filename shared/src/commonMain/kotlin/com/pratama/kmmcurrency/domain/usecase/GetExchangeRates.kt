package com.pratama.kmmcurrency.domain.usecase

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.core.BaseEmptyUseCase
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.core.measureTimeMillis
import com.pratama.kmmcurrency.domain.entity.Rate
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetExchangeRates(private val repo: OpenExchangeRepository) :
    BaseEmptyUseCase<Result<List<Rate>>> {

    override suspend fun invoke(): Result<List<Rate>> {
        measureTimeMillis({ time -> Logger.i { "execution time get exchanges rates $time ms" } }, {
            return repo.getRates()
        })
    }
}