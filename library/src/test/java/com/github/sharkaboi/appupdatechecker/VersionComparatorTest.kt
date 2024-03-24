package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.fdroid.FDroidVersionCodeSource
import com.sharkaboi.appupdatechecker.sources.github.GithubTagSource
import com.sharkaboi.appupdatechecker.sources.json.JsonVersionNameSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
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

    @Test
    fun `Setting custom version comparator returns proper update status`() =
        runBlocking {
            val source = JsonVersionNameSource(
                jsonEndpoint = "https://gist.github.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/1b99f59babe56a63aa95a7fb31b5d3682c4b18db/test-custom.json",
                currentVersion = "v1.0-alpha"
            )

            val customVersionComparator = object : VersionComparator<String> {
                override fun isNewerVersion(
                    currentVersion: String,
                    newVersion: String
                ): Boolean {
                    return DefaultStringVersionComparator.isNewerVersion(
                        currentVersion.substringBefore('-'),
                        newVersion.substringBefore('-')
                    )
                }
            }
            source.setCustomVersionComparator(customVersionComparator)
            val testChecker = AppUpdateChecker(source = source)
            val result = testChecker.checkUpdate()
            println(result)
            assert(result is UpdateResult.UpdateAvailable<*>)
        }
}