package com.sharkaboi.appupdatechecker.mappers

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import com.sharkaboi.appupdatechecker.sources.github.GithubResponse
import com.sharkaboi.appupdatechecker.sources.json.JsonResponse

internal fun GithubResponse.toUpdateAvailableState(source: AppUpdateCheckerSource): UpdateState.UpdateAvailable {
    return UpdateState.UpdateAvailable(
        releaseNotes = body,
        latestVersionUrl = htmlUrl,
        latestVersion = tagName,
        source = source
    )
}

internal fun JsonResponse.toUpdateAvailableState(source: AppUpdateCheckerSource): UpdateState.UpdateAvailable {
    return UpdateState.UpdateAvailable(
        releaseNotes = releaseNotes,
        latestVersionUrl = latestVersionUrl,
        latestVersion = latestVersion,
        source = source
    )
}