package com.sharkaboi.appupdatechecker.models

sealed class AppUpdateCheckerSource {

    data class GithubSource(
        val ownerUsername: String,
        val repoName: String
    ) : AppUpdateCheckerSource()

    data class FDroidSource(
        val packageName: String? = null
    ) : AppUpdateCheckerSource()

    data class JsonSource(
        val jsonEndpoint: String
    ) : AppUpdateCheckerSource()

    data class XMLSource(
        val xmlEndpoint: String
    ) : AppUpdateCheckerSource()
}
