package com.sharkaboi.appupdatechecker.models

sealed class UpdateState {

    data class UpdateAvailable(
        val latestVersion: String,
        val latestVersionUrl: String,
        val releaseNotes: String?,
        val source: AppUpdateCheckerSource
    ) : UpdateState()

    // Latest version already installed
    object LatestVersionInstalled : UpdateState()

    // Google Play returned "Varies by device"
    object GooglePlayInvalid : UpdateState()

    // GitHub user or repo is empty string
    object GithubMalformed : UpdateState()

    // GitHub repo is private or no releases found of matching type
    object GithubInvalid : UpdateState()

    // No Internet connection available
    object NoNetworkFound : UpdateState()

    // URL for the XML file is not valid
    object XMLMalformed : UpdateState()

    // XML file is invalid or is unreachable
    object XMLInvalid : UpdateState()

    // URL for the JSON file is not valid
    object JSONMalformed : UpdateState()

    // URL for the JSON file is not valid
    object JSONInvalid : UpdateState()

    // Generic UpdateState type to handle other UpdateStates
    data class GenericUpdateState(
        val exception: Exception
    ) : UpdateState()

}

