package com.pratama.kmmcurrency.data

import co.touchlab.kermit.Logger
import com.pratama.kmmcurrency.data.local.dao.FetcherDao
import kotlinx.datetime.Clock

class FetchChecker(private val fetcherDao: FetcherDao) {

    init {
        Logger.setTag("fetch_checker")
    }

    companion object {
        const val THRESHOLD = 1800 // 30 minute
    }

    fun insertFetch(key: String) {
        val timestamp = Clock.System.now().epochSeconds
        Logger.d { "insert fetch key $key $timestamp" }
        fetcherDao.insertLastFetch(key, timestamp = timestamp)
    }

    fun canFetch(key: String, currentTimestamp: Long = Clock.System.now().epochSeconds): Boolean {
//        val result = (currentTimestamp - fetcherDao.getLastFetch(key))
//        Logger.d { "canfetch $currentTimestamp - ${fetcherDao.getLastFetch(key)} result $result" }
//        return (currentTimestamp - fetcherDao.getLastFetch(key)) > THRESHOLD
        return false
    }
}