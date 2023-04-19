package com.pratama.kmmcurrency

import com.pratama.kmmcurrency.data.FetchChecker
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class FetchCheckerTest {

//    private lateinit var fetchChecker: FetchChecker
//    private lateinit var fakeFetcherDao: FakeFetcherDao
//
//    @BeforeTest
//    fun setup() {
//        fakeFetcherDao = FakeFetcherDao()
//        fetchChecker = FetchChecker(fakeFetcherDao)
//    }
//
//    @Test
//    fun `test can fetch should be return true`() = runTest {
//        val lastFetch = 1681871425L //  April 19, 2023 9:30:25 AM
//        val currentTime = 1681873332L // April 19, 2023 10:02:12
//
//        fakeFetcherDao.setupLastFetch(lastFetch)
//        val result = fetchChecker.canFetch("key", currentTime)
//
//        assertTrue(result)
//    }
//
//
//    @Test
//    fun `test can fetch should be return false`() = runTest {
//        val lastFetch = 1681872625L //  April 19, 2023 9:50:25
//        val currentTime = 1681873332L // April 19, 2023 10:02:12
//
//        fakeFetcherDao.setupLastFetch(lastFetch)
//        val result = fetchChecker.canFetch("key", currentTime)
//        assertTrue(!result)
//    }
}