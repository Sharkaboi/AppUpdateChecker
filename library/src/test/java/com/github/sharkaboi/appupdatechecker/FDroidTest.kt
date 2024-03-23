package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.InvalidPackageNameException
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.fdroid.FDroidVersionCodeSource
import com.sharkaboi.appupdatechecker.sources.fdroid.FDroidVersionNameSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FDroidTest {

    private val packageName = "org.fdroid.fdroid"

    @Test
    fun `Checker on older installed version returns new version`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = FDroidVersionNameSource(
                packageName = packageName,
                currentVersion = "v0.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assert(versionNameResult is UpdateResult.UpdateAvailable<*>)

        val versionCodeChecker = AppUpdateChecker(
            source = FDroidVersionCodeSource(
                packageName = packageName,
                currentVersion = 0
            )
        )
        val versionCodeResult = versionCodeChecker.checkUpdate()
        println(versionCodeResult)
        assert(versionCodeResult is UpdateResult.UpdateAvailable<*>)
    }

    @Test
    fun `Checker on newer installed version returns no update`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = FDroidVersionNameSource(
                packageName = packageName,
                currentVersion = "v${Long.MAX_VALUE}.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assert(versionNameResult is UpdateResult.NoUpdate)

        val versionCodeChecker = AppUpdateChecker(
            source = FDroidVersionCodeSource(
                packageName = packageName,
                currentVersion = Long.MAX_VALUE
            )
        )
        val versionCodeResult = versionCodeChecker.checkUpdate()
        println(versionCodeResult)
        assert(versionCodeResult is UpdateResult.NoUpdate)
    }

    @Test
    fun `Checker on invalid fdroid package name returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = FDroidVersionCodeSource(
                    packageName = "invalid.app.package.name.fdroid",
                    currentVersion = 0
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertNotNull(exception)
    }

    @Test
    fun `Checker on blank package name returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = FDroidVersionCodeSource(
                    packageName = "   ",
                    currentVersion = 0
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is InvalidPackageNameException)
    }

    @Test
    fun `Checker on repo name without dot returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = FDroidVersionCodeSource(
                    packageName = "comsimplemobiletoolsgallerypro",
                    currentVersion = 0
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertNotNull(exception)
    }
}
