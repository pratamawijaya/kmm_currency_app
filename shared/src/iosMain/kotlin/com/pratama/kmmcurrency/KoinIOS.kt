package com.pratama.kmmcurrency

import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual val platformModule = module {

    single<SqlDriver> {
        NativeSqliteDriver(CurrencyDatabase.Schema, "CurrencyDB")
    }

    single<DecimalFormat> {
        DecimalFormatImplIOS()
    }

}