package com.pratama.kmmcurrency.data.local.dao

interface FetcherDao {
    fun insertLastFetch(key: String, timestamp: Long)
    fun getLastFetch(key: String): Long?
}