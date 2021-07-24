package com.sharkaboi.appupdatechecker.models

sealed interface IAppUpdateCheckerSource
sealed class AppUpdateCheckerSource : IAppUpdateCheckerSource {

    data class GithubSource(
        val ownerUsername: String,
        val repoName: String
    ) : AppUpdateCheckerSource()

    data class GitlabSource(
        val ownerUsername: String,
        val repoName: String,
        val shouldIncludePreReleases: Boolean = false
    ) : AppUpdateCheckerSource()

    object AmazonSource : AppUpdateCheckerSource()

    data class FDroidSource(
        val packageName: String? = null
    ) : AppUpdateCheckerSource()

    object GooglePlaySource : AppUpdateCheckerSource()

    data class JsonSource(
        val jsonEndpoint: String
    ) : AppUpdateCheckerSource()

    data class XMLSource(
        val xmlEndpoint: String
    ) : AppUpdateCheckerSource()

}

