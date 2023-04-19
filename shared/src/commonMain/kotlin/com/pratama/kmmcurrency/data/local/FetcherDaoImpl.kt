package com.pratama.kmmcurrency.data.local

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.cache.AppDatabaseQueries
import com.pratama.kmmcurrency.cache.BaseDao
import com.pratama.kmmcurrency.cache.CurrencyDatabase
import com.pratama.kmmcurrency.data.local.dao.FetcherDao

class FetcherDaoImpl(private val database: CurrencyDatabase) : FetcherDao, BaseDao() {

    override val queries: AppDatabaseQueries
        get() = database.appDatabaseQueries

    override fun insertLastFetch(key: String, timestamp: Long) {
        queries.insertFetch(key, timestamp.toDouble())
    }

    override fun getLastFetch(key: String): Long? {
        return queries.selectFetchByKey(key).executeAsOneOrNull()?.lastfetch?.toLong()
    }
}

