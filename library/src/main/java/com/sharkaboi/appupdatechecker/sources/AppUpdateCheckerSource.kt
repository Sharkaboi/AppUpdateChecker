package com.sharkaboi.appupdatechecker.sources

import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidEndPointException
import com.sharkaboi.appupdatechecker.models.InvalidPackageNameException
import com.sharkaboi.appupdatechecker.models.InvalidRepositoryNameException
import com.sharkaboi.appupdatechecker.models.InvalidUserNameException
import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.versions.VersionComparator

abstract class AppUpdateCheckerSource<T> {
    protected abstract val currentVersion: T
    protected abstract var versionComparator: VersionComparator<T>
    protected abstract suspend fun queryVersionDetails(): VersionDetails<T>

    @Throws(
        InvalidVersionException::class,
        InvalidPackageNameException::class,
        PackageNotFoundException::class,
        InvalidUserNameException::class,
        InvalidRepositoryNameException::class,
        InvalidEndPointException::class,
        RemoteError::class,
        GenericError::class
    )
    suspend fun getUpdateState(): UpdateResult {
        val versionDetails = queryVersionDetails()
        if (versionComparator.isNewerVersion(currentVersion, versionDetails.latestVersion)) {
            return UpdateResult.UpdateAvailable(versionDetails)
        }

        return UpdateResult.NoUpdate
    }

    fun setCustomVersionComparator(comparator: VersionComparator<T>) {
        this.versionComparator = comparator
    }
}