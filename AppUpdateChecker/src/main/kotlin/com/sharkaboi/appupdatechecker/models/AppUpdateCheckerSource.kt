package com.sharkaboi.appupdatechecker.models

sealed class AppUpdateCheckerSource {

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

    object FDroidSource : AppUpdateCheckerSource()

    object GooglePlaySource : AppUpdateCheckerSource()

    data class JsonSource(
        val jsonEndpoint: String
    ) : AppUpdateCheckerSource()

    data class XMLSource(
        val xmlEndpoint: String
    ) : AppUpdateCheckerSource()

}

