package com.pratama.kmmcurrency.core

import kotlinx.datetime.Clock

interface BaseUseCase<in Input : Any, out Output : Any> {
    suspend operator fun invoke(input: Input): Output
}

interface BaseEmptyUseCase<out Output : Any> {
    suspend operator fun invoke(): Output
}

internal inline fun <T> measureTimeMillis(
    loggingFunction: (Long) -> Unit,
    fn: () -> T
): T {
    val start = Clock.System.now().toEpochMilliseconds()
    val result: T = fn.invoke()
    loggingFunction.invoke(Clock.System.now().toEpochMilliseconds() - start)
    return result
}

internal inline fun <T> measureTimeNano(
    loggingFunction: (Int) -> Unit,
    fn: () -> T
): T {
    val start = Clock.System.now().nanosecondsOfSecond
    val result: T = fn.invoke()
    loggingFunction.invoke(Clock.System.now().nanosecondsOfSecond - start)
    return result
}