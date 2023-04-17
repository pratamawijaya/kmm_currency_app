package com.pratama.kmmcurrency

import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            CurrencyDatabase.Schema,
            get(),
            "CurrencyDB"
        )
    }
}