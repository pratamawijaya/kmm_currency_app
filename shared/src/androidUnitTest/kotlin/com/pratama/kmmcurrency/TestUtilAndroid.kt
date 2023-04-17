package com.pratama.kmmcurrency

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal actual fun testDbConnection(): SqlDriver {
    return try {
        val app = ApplicationProvider.getApplicationContext<Application>()
        AndroidSqliteDriver(CurrencyDatabase.Schema, app, "CurrencyDB")
    } catch (exception: IllegalStateException) {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            CurrencyDatabase.Schema.create(it)
        }
    }
}