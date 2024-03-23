package com.sharkaboi.appupdatechecker.models

data class VersionDetails<T>(
    val latestVersion: T,
    val latestVersionUrl: String,
    val releaseNotes: String?,
)
