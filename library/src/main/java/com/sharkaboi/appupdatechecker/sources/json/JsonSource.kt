package com.sharkaboi.appupdatechecker.sources.json

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerException
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidEndPointException
import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.DefaultVersionCodeComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


sealed class JsonSource<T> : AppUpdateCheckerSource<T>() {
    abstract val jsonEndpoint: String

    private val service = Retrofit.Builder()
        .baseUrl("https://google.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(JsonService::class.java)

    protected suspend fun queryResponse(): JsonResponse {
        if (HttpUrl.parse(jsonEndpoint) == null) {
            throw InvalidEndPointException("Invalid endpoint $jsonEndpoint")
        }

        try {
            val response = service.getJsonReleaseMetaData(url = jsonEndpoint)
            return response.body()
                ?: throw RemoteError(Throwable(response.errorBody()?.string()))
        } catch (e: Exception) {
            if (e is AppUpdateCheckerException) {
                throw e
            }
            throw GenericError(e)
        }
    }
}

data class JsonVersionNameSource(
    override val jsonEndpoint: String,
    override val currentVersion: String,
    override var versionComparator: VersionComparator<String> = DefaultStringVersionComparator
) : JsonSource<String>() {
    override suspend fun queryVersionDetails(): VersionDetails<String> {
        val response = queryResponse()
        val latestVersion = response.latestVersionName
            ?: throw InvalidVersionException("Version name was not found or null in response")
        return VersionDetails(
            releaseNotes = response.releaseNotes,
            latestVersionUrl = response.latestVersionUrl,
            latestVersion = latestVersion,
        )
    }
}

data class JsonVersionCodeSource(
    override val jsonEndpoint: String,
    override val currentVersion: Long,
    override var versionComparator: VersionComparator<Long> = DefaultVersionCodeComparator
) : JsonSource<Long>() {
    override suspend fun queryVersionDetails(): VersionDetails<Long> {
        val response = queryResponse()
        val latestVersion = response.latestVersionCode
            ?: throw InvalidVersionException("Version code was not found or null in response")
        return VersionDetails(
            releaseNotes = response.releaseNotes,
            latestVersionUrl = response.latestVersionUrl,
            latestVersion = latestVersion,
        )
    }
}