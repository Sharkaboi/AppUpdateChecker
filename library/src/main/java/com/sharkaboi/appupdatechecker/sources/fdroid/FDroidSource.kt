package com.sharkaboi.appupdatechecker.sources.fdroid

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerException
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidPackageNameException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.DefaultVersionCodeComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


sealed class FDroidSource<T> : AppUpdateCheckerSource<T>() {
    abstract val packageName: String

    private val service = Retrofit.Builder()
        .baseUrl(FdroidConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(FDroidService::class.java)

    protected suspend fun queryResponse(): FDroidResponse.Package {
        if (packageName.isBlank()) {
            throw InvalidPackageNameException("Invalid package name $packageName")
        }

        if (!packageName.contains('.')) {
            throw InvalidPackageNameException("Invalid package name $packageName")
        }
        try {

            val response = service.getReleases(packageName = packageName)
            if (response.code() == 404) {
                throw PackageNotFoundException("No details found for package name $packageName")
            }

            val release = response.body()
                ?: throw RemoteError(Throwable(response.errorBody()?.string()))
            return release.packages.firstOrNull()
                ?: throw PackageNotFoundException("No details found for package name $packageName")
        } catch (e: Exception) {
            if (e is AppUpdateCheckerException) {
                throw e
            }
            throw GenericError(e)
        }
    }
}

data class FDroidVersionNameSource(
    override val packageName: String,
    override val currentVersion: String,
    override var versionComparator: VersionComparator<String> = DefaultStringVersionComparator
) : FDroidSource<String>() {

    override suspend fun queryVersionDetails(): VersionDetails<String> {
        val response = queryResponse()
        return VersionDetails(
            releaseNotes = null,
            latestVersionUrl = FdroidConstants.HTML_BASE_URL + this.packageName,
            latestVersion = response.versionName,
        )
    }
}

data class FDroidVersionCodeSource(
    override val packageName: String,
    override val currentVersion: Long,
    override var versionComparator: VersionComparator<Long> = DefaultVersionCodeComparator
) : FDroidSource<Long>() {

    override suspend fun queryVersionDetails(): VersionDetails<Long> {
        val response = queryResponse()
        return VersionDetails(
            releaseNotes = null,
            latestVersionUrl = FdroidConstants.HTML_BASE_URL + this.packageName,
            latestVersion = response.versionCode,
        )
    }
}