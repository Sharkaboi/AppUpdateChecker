package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.InvalidRepositoryNameException
import com.sharkaboi.appupdatechecker.models.InvalidUserNameException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.github.GithubTagSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class GithubTest {
    private val ownerUsername = "Sharkaboi"
    private val repoName = "MediaHub"

    @Test
    fun `Checker on older installed version returns new version`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = GithubTagSource(
                ownerUsername = ownerUsername,
                repoName = repoName,
                currentVersion = "v0.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assertTrue(versionNameResult is UpdateResult.UpdateAvailable<*>)
    }

    @Test
    fun `Checker on newer installed version returns no update`() = runBlocking {
        val versionNameChecker = AppUpdateChecker(
            source = GithubTagSource(
                ownerUsername = ownerUsername,
                repoName = repoName,
                currentVersion = "v${Long.MAX_VALUE}.0.0"
            )
        )
        val versionNameResult = versionNameChecker.checkUpdate()
        println(versionNameResult)
        assertTrue(versionNameResult is UpdateResult.NoUpdate)
    }

    @Test
    fun `Checker on invalid github repo returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = GithubTagSource(
                    ownerUsername = "Sharkaboi",
                    repoName = "adadadadadadadadaadada",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is PackageNotFoundException)
    }

    @Test
    fun `Checker on invalid github user returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = GithubTagSource(
                    ownerUsername = "adadadadadadadadaadada",
                    repoName = "MediaHub",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is PackageNotFoundException)
    }

    @Test
    fun `Checker on github with no release returns invalid error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = GithubTagSource(
                    ownerUsername = "Sharkaboi",
                    repoName = "sharkaboi.github.io",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is PackageNotFoundException)
    }

    @Test
    fun `Checker on blank username returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = GithubTagSource(
                    ownerUsername = "   ",
                    repoName = "sharkaboi.github.io",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is InvalidUserNameException)
    }

    @Test
    fun `Checker on empty repo name returns malformed error`() = runBlocking {
        val exception = runCatching {
            val testChecker = AppUpdateChecker(
                source = GithubTagSource(
                    ownerUsername = "Sharkaboi",
                    repoName = "",
                    currentVersion = "v0.0.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
        }.exceptionOrNull()
        println(exception)
        assertTrue(exception is InvalidRepositoryNameException)
    }
}
