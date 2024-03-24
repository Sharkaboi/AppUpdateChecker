package com.sharkaboi.appupdatechecker.sources.fdroid

import com.sharkaboi.appupdatechecker.models.InvalidPackageNameException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
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
        .create(FdroidService::class.java)

    protected suspend fun queryResponse(): FDroidResponse.Package {
        if (packageName.isBlank()) {
            throw InvalidPackageNameException("Invalid package name $packageName")
        }

        if (!packageName.contains('.')) {
            throw InvalidPackageNameException("Invalid package name $packageName")
        }

        val release = service.getReleases(packageName = packageName)

        return release.packages.firstOrNull()
            ?: throw PackageNotFoundException("No details found for package name $packageName")
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