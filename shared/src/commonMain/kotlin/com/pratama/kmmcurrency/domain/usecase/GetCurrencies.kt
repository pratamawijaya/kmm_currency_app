package com.pratama.kmmcurrency.domain.usecase

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.core.BaseEmptyUseCase
import com.pratama.kmmcurrency.core.BaseUseCase
import com.pratama.kmmcurrency.core.measureTimeMillis
import com.pratama.kmmcurrency.domain.entity.Currency
import com.pratama.kmmcurrency.domain.repository.OpenExchangeRepository

class GetCurrencies(private val repo: OpenExchangeRepository) :
    BaseEmptyUseCase<Result<List<Currency>>> {

    override suspend fun invoke(): Result<List<Currency>> {
        measureTimeMillis({ time -> Logger.i { "execution time get currencies $time ms" } }, {
            return repo.getCurrencies()
        })
    }
}