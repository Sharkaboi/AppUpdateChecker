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

class AppUpdateCheckerTest {
    private lateinit var context: Context

    @Before
    fun init() {
        context = mockk(relaxed = true)
        mockkStatic("com.sharkaboi.appupdatechecker.extensions.ContextExtensionsKt")
    }

    @Test
    fun `Checker on invalid current version tag returns generic error`() = runBlocking {
        every { context.isInternetConnected } returns true
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
        assert(result is UpdateState.GenericError)
    }

    @Test
    fun `Checker on no internet returns network error`() = runBlocking {
        every { context.isInternetConnected } returns false
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.GithubSource(
                ownerUsername = "Sharkaboi",
                repoName = "MediaHub"
            ),
            currentVersionTag = "v1.1"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.NoNetworkFound)
    }
}