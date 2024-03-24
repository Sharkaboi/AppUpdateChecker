package com.github.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.AppUpdateChecker
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerException
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidEndPointException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class CustomSourceTest {

    class CustomVersionSource(
        override val currentVersion: String,
        override var versionComparator: VersionComparator<String> = DefaultStringVersionComparator
    ) : AppUpdateCheckerSource<String>() {
        private val customSource =
            "https://gist.githubusercontent.com/Sharkaboi/66b45a22afde23a9b2781eeec6f10c56/raw/087bdb3151dc54eda2e7a98e88f1264184972313/custom-source/"

        interface CustomService {
            @GET
            suspend fun getReleaseVersion(
                @Url url: String
            ): Response<String>
        }

        private val service = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(CustomService::class.java)

        override suspend fun queryVersionDetails(): VersionDetails<String> {
            if (HttpUrl.parse(customSource) == null) {
                throw InvalidEndPointException("Invalid endpoint $customSource")
            }

            try {
                val response = service.getReleaseVersion(customSource)
                val version = response.body()
                    ?: throw RemoteError(Throwable(response.errorBody()?.string()))
                return VersionDetails(
                    latestVersion = version,
                    latestVersionUrl = "https://mywebsite.com/download.apk",
                    releaseNotes = null
                )
            } catch (e: Exception) {
                if (e is AppUpdateCheckerException) {
                    throw e
                }
                throw GenericError(e)
            }
        }
    }

    @Test
    fun `Setting custom version comparator returns proper update status`() =
        runBlocking {
            val testChecker = AppUpdateChecker(
                source = CustomVersionSource(
                    currentVersion = "v1.0"
                )
            )
            val result = testChecker.checkUpdate()
            println(result)
            Assert.assertTrue(result is UpdateResult.UpdateAvailable<*>)
        }
}