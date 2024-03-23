package com.sharkaboi.appupdatechecker.sources.github

import com.sharkaboi.appupdatechecker.models.InvalidRepositoryNameException
import com.sharkaboi.appupdatechecker.models.InvalidUserNameException
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class GithubTagSource(
    val ownerUsername: String,
    val repoName: String,
    override val currentVersion: String,
    override val versionComparator: VersionComparator<String> = DefaultStringVersionComparator
) : AppUpdateCheckerSource<String>() {

    private val service = Retrofit.Builder()
        .baseUrl(GithubConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(GithubService::class.java)

    override suspend fun queryVersionDetails(): VersionDetails<String> {
        if (ownerUsername.isBlank()) {
            throw InvalidUserNameException("Invalid username $ownerUsername")
        }

        if (repoName.isBlank()) {
            throw InvalidRepositoryNameException("Invalid repository name $repoName")
        }

        val response = service.getLatestRelease(owner = ownerUsername, repo = repoName)

        return VersionDetails(
            releaseNotes = response.body,
            latestVersionUrl = response.htmlUrl,
            latestVersion = response.tagName,
        )
    }
}