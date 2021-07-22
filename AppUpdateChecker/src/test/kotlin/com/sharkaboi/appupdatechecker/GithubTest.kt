package com.sharkaboi.appupdatechecker

import android.content.Context
import com.sharkaboi.appupdatechecker.extensions.isInternetConnected
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GithubTest {
    private lateinit var context: Context

    @Before
    fun init() {
        context = mockk(relaxed = true)
        mockkStatic("com.sharkaboi.appupdatechecker.extensions.ContextExtensionsKt")
        every { context.isInternetConnected } returns true
    }

    @Test
    fun `Checker on older installed version returns new version`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "MediaHub"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.UpdateAvailable)
    }

    @Test
    fun `Checker on newer installed version returns no update`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "MediaHub"
            ),
            currentVersionTag = "v${Int.MAX_VALUE}.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.LatestVersionInstalled)
    }

    @Test
    fun `Checker on invalid github repo returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "adadadadadadadadaadada"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubInvalid)
    }

    @Test
    fun `Checker on invalid github user returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "adadadadadadadadaadada",
                repoName = "MediaHub"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubInvalid)
    }

    @Test
    fun `Checker on github with no release returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "sharkaboi.github.io"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubInvalid)
    }

    @Test
    fun `Checker on blank username returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "   ",
                repoName = "sharkaboi.github.io"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubMalformed)
    }

    @Test
    fun `Checker on empty repo name returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = ""
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubMalformed)
    }

    @Test
    fun `Checker on invalid tag name returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "MediaHub"
            ),
            currentVersionTag = "vdasd.ada.adas.jj"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.GithubInvalid)
    }
}