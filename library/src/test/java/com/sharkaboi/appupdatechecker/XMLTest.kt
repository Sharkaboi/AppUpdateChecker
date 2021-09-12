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

class XMLTest {
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
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/132296a0e85d254cd982959bc8f198ca61c213b7/test.xml"
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
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/132296a0e85d254cd982959bc8f198ca61c213b7/test.xml"
            ),
            currentVersionTag = "v${Int.MAX_VALUE}.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.LatestVersionInstalled)
    }

    @Test
    fun `Checker on invalid xml repo returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "https://google.com"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.XMLInvalid)
    }

    @Test
    fun `Checker on invalid xml schema returns invalid error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/132296a0e85d254cd982959bc8f198ca61c213b7/invalid.xml"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.XMLInvalid)
    }

    @Test
    fun `Checker on invalid url returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "invalid url"
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.XMLMalformed)
    }

    @Test
    fun `Checker on blank endpoint returns malformed error`() = runBlocking {
        val testChecker = AppUpdateChecker(
            context,
            source = AppUpdateCheckerSource.XMLSource(
                xmlEndpoint = "   "
            ),
            currentVersionTag = "v0.0.0"
        )
        val result = testChecker.checkUpdate()
        println(result)
        assert(result is UpdateState.XMLMalformed)
    }
}
