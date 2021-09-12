package com.sharkaboi.appupdatechecker.sources.fdroid

import com.sharkaboi.appupdatechecker.extensions.isAfterVersion

internal fun FdroidResponse.isAfterVersion(currentVersion: String): Boolean {
    require(this.packages.isNotEmpty()) { "No package found in FDroid for $packageName" }
    val latestVersion = this.packages.first()
    return latestVersion.versionName.isAfterVersion(currentVersion)
}
