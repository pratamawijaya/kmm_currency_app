package com.pratama.kmmcurrency.core

interface BaseUseCase<in Input : Any, out Output : Any> {
    suspend operator fun invoke(input: Input): Output
}

interface BaseEmptyUseCase<out Output : Any> {
    suspend operator fun invoke(): Output
}