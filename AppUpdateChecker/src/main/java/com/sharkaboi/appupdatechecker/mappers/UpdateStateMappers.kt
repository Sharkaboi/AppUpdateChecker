package com.sharkaboi.appupdatechecker.mappers

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import com.sharkaboi.appupdatechecker.sources.fdroid.FdroidConstants
import com.sharkaboi.appupdatechecker.sources.fdroid.FdroidResponse
import com.sharkaboi.appupdatechecker.sources.github.GithubResponse
import com.sharkaboi.appupdatechecker.sources.json.JsonResponse
import com.sharkaboi.appupdatechecker.sources.xml.XMLResponse

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

internal fun FdroidResponse.toUpdateAvailableState(source: AppUpdateCheckerSource): UpdateState.UpdateAvailable {
    return UpdateState.UpdateAvailable(
        releaseNotes = null,
        latestVersionUrl = FdroidConstants.HTML_BASE_URL + this.packageName,
        latestVersion = this.packages.first().versionName,
        source = source
    )
}

internal fun XMLResponse.toUpdateAvailableState(source: AppUpdateCheckerSource): UpdateState.UpdateAvailable {
    return UpdateState.UpdateAvailable(
        releaseNotes = this.releaseNotes,
        latestVersionUrl = this.latestVersionUrl,
        latestVersion = this.latestVersion,
        source = source
    )
}
