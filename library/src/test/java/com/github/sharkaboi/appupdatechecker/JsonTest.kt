package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidEndPointException
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.json.JsonVersionCodeSource
import com.sharkaboi.appupdatechecker.sources.json.JsonVersionNameSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class JsonTest {
    private val jsonEndpoint =
        "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/3b168fc906490cc7f3cf0fc2b843461abf52422e/test.json"

    @Test
    fun `Checker on older installed version returns new version`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = JsonVersionNameSource(
                jsonEndpoint = jsonEndpoint,
                currentVersion = "v0.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assertTrue(versionNameResult is UpdateResult.UpdateAvailable<*>)

        val versionCodeChecker = AppUpdateChecker(
            source = JsonVersionCodeSource(
                jsonEndpoint = jsonEndpoint,
                currentVersion = 0
            )
        )
        val versionCodeResult = versionCodeChecker.checkUpdate()
        println(versionCodeResult)
        assertTrue(versionCodeResult is UpdateResult.UpdateAvailable<*>)
    }

    @Test
    fun `Checker on newer installed version returns no update`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = JsonVersionNameSource(
                jsonEndpoint = jsonEndpoint,
                currentVersion = "v${Long.MAX_VALUE}.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assertTrue(versionNameResult is UpdateResult.NoUpdate)

        val versionCodeChecker = AppUpdateChecker(
            source = JsonVersionCodeSource(
                jsonEndpoint = jsonEndpoint,
                currentVersion = Long.MAX_VALUE
            )
        )
        val versionCodeResult = versionCodeChecker.checkUpdate()
        println(versionCodeResult)
        assertTrue(versionCodeResult is UpdateResult.NoUpdate)
    }


    @Test
    fun `Checker on invalid json repo returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = JsonVersionNameSource(
                    jsonEndpoint = "https://google.com",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is GenericError)
    }

    @Test
    fun `Checker on invalid json schema returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = JsonVersionNameSource(
                    jsonEndpoint = "https://gist.github.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/3b168fc906490cc7f3cf0fc2b843461abf52422e/invalid.json",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is GenericError)
    }

    @Test
    fun `Checker on invalid url returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = JsonVersionNameSource(
                    jsonEndpoint = "invalid url",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is InvalidEndPointException)
    }

    @Test
    fun `Checker on blank endpoint returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = JsonVersionNameSource(
                    jsonEndpoint = "                   ",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is InvalidEndPointException)
    }
}
