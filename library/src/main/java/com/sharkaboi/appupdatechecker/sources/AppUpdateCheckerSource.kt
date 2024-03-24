package com.sharkaboi.appupdatechecker.sources

import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.versions.VersionComparator

abstract class AppUpdateCheckerSource<T> {
    protected abstract val currentVersion: T
    protected abstract var versionComparator: VersionComparator<T>
    protected abstract suspend fun queryVersionDetails(): VersionDetails<T>

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