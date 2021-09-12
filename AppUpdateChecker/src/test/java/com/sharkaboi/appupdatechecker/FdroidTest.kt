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

class FdroidTest {
    private lateinit var context: Context

    @Before
    fun init() {
        context = mockk(relaxed = true)
        mockkStatic("com.sharkaboi.appupdatechecker.extensions.ContextExtensionsKt")
        every { context.isInternetConnected } returns true
        every { context.packageName } returns "com.simplemobiletools.gallery.pro"
    }

    @Test
    fun `Checker on older installed version returns new version`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.FDroidSource(),
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
            source = AppUpdateCheckerSource.FDroidSource(),
            currentVersionTag = "v${Int.MAX_VALUE}.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.LatestVersionInstalled)
    }

    @Test
    fun `Checker on invalid fdroid package name returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.FDroidSource(
                packageName = "invalid.app.package.name.fdroid",
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.FDroidInvalid)
    }

    @Test
    fun `Checker on blank package name returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.FDroidSource(
                packageName = "   "
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.FDroidMalformed)
    }

    @Test
    fun `Checker on repo name without dot returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.FDroidSource(
                packageName = "comsimplemobiletoolsgallerypro"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.FDroidMalformed)
    }
}
