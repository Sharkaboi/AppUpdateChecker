package com.sharkaboi.appupdatechecker.versions

import com.sharkaboi.appupdatechecker.models.InvalidVersionException

object DefaultVersionCodeComparator : VersionComparator<Long> {
    override fun isNewerVersion(
        currentVersion: Long,
        newVersion: Long,
    ): Boolean {
        if (currentVersion < 0) throw InvalidVersionException("Invalid current version $currentVersion")

        if (newVersion < 0) throw InvalidVersionException("Invalid new version $newVersion")

        return newVersion > currentVersion
    }
}
