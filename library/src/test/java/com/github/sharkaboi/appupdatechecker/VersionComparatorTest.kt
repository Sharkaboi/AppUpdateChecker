package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import com.sharkaboi.appupdatechecker.sources.fdroid.FDroidVersionCodeSource
import com.sharkaboi.appupdatechecker.sources.github.GithubTagSource
import kotlinx.coroutines.runBlocking
import org.junit.Test

class VersionComparatorTest {

    @Test
    fun `Checker on invalid current version name returns invalid version name error`() =
        runBlocking {
            val exception = runCatching {
                val testChecker = AppUpdateChecker(
                    source = GithubTagSource(
                        ownerUsername = "Sharkaboi",
                        repoName = "MediaHub",
                        currentVersion = "vdasd.ada.adas.jj"
                    )
                )
                val result = testChecker.checkUpdate()
                println(result)
            }.exceptionOrNull()
            println(exception)
            assert(exception is InvalidVersionException)
        }

    @Test
    fun `Checker on invalid current version name ending returns invalid version name error`() =
        runBlocking {
            val exception = runCatching {
                val testChecker = AppUpdateChecker(
                    source = GithubTagSource(
                        ownerUsername = "Sharkaboi",
                        repoName = "MediaHub",
                        currentVersion = "1."
                    )
                )
                val result = testChecker.checkUpdate()
                println(result)
            }.exceptionOrNull()
            println(exception)
            assert(exception is InvalidVersionException)
        }

    @Test
    fun `Checker on invalid current version name start returns invalid version name error`() =
        runBlocking {
            val exception = runCatching {
                val testChecker = AppUpdateChecker(
                    source = GithubTagSource(
                        ownerUsername = "Sharkaboi",
                        repoName = "MediaHub",
                        currentVersion = ".1.2"
                    )
                )
                val result = testChecker.checkUpdate()
                println(result)
            }.exceptionOrNull()
            println(exception)
            assert(exception is InvalidVersionException)
        }

    @Test
    fun `Checker on invalid current version name char returns invalid version name error`() =
        runBlocking {
            val exception = runCatching {
                val testChecker = AppUpdateChecker(
                    source = GithubTagSource(
                        ownerUsername = "Sharkaboi",
                        repoName = "MediaHub",
                        currentVersion = "g.1.2"
                    )
                )
                val result = testChecker.checkUpdate()
                println(result)
            }.exceptionOrNull()
            println(exception)
            assert(exception is InvalidVersionException)
        }

    @Test
    fun `Checker on invalid current version code returns invalid version code error`() =
        runBlocking {
            val exception = runCatching {
                val testChecker = AppUpdateChecker(
                    source = FDroidVersionCodeSource(
                        packageName = "org.fdroid.fdroid",
                        currentVersion = -5000
                    )
                )
                val result = testChecker.checkUpdate()
                println(result)
            }.exceptionOrNull()
            println(exception)
            assert(exception is InvalidVersionException)
        }
}