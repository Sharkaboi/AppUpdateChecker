package com.sharkaboi.appupdatechecker.versions

import com.sharkaboi.appupdatechecker.models.InvalidVersionException

interface VersionComparator<T> {
    @Throws(InvalidVersionException::class)
    fun isNewerVersion(
        currentVersion: T,
        newVersion: T,
    ): Boolean
}
