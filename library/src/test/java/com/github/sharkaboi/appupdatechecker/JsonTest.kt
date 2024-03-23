package com.github.sharkaboi.appupdatechecker

import android.content.Context
import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.extensions.isInternetConnected
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class JsonTest {
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
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/e0efd6ebdaf88615945fc8c7727a7fc620ad61bf/test.json"
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
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/e0efd6ebdaf88615945fc8c7727a7fc620ad61bf/test.json"
            ),
            currentVersionTag = "v${Int.MAX_VALUE}.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.LatestVersionInstalled)
    }

    @Test
    fun `Checker on invalid json repo returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "https://google.com"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.JSONInvalid)
    }

    @Test
    fun `Checker on invalid json schema returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/a3998cc1728cd42ba02b5b9789f505de42090c84/invalid.json"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.JSONInvalid)
    }

    @Test
    fun `Checker on invalid url returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "invalid url"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.JSONMalformed)
    }

    @Test
    fun `Checker on blank endpoint returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.JsonSource(
                jsonEndpoint = "   "
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.JSONMalformed)
    }
}
