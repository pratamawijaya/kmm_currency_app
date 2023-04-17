package com.pratama.kmmcurrency.core

import com.pratama.kmmcurrency.di.openExchangeModule
import com.pratama.kmmcurrency.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import kotlin.jvm.JvmStatic

object CoreApp {

    @JvmStatic
    fun initialize(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
        return startKoin {
            appDeclaration()
            modules(getCoreModules())
        }
    }

    private fun getCoreModules(): List<Module> {
        return listOf(
            openExchangeModule,
            platformModule
        )
    }
}