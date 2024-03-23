package com.sharkaboi.appupdatechecker.models

sealed interface UpdateResult {
    data class UpdateAvailable<T>(
        val versionDetails: VersionDetails<T>
    ) : UpdateResult

    data object NoUpdate : UpdateResult
}
