package com.sharkaboi.appupdatechecker.sources.github

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerException
import com.sharkaboi.appupdatechecker.models.GenericError
import com.sharkaboi.appupdatechecker.models.InvalidRepositoryNameException
import com.sharkaboi.appupdatechecker.models.InvalidUserNameException
import com.sharkaboi.appupdatechecker.models.PackageNotFoundException
import com.sharkaboi.appupdatechecker.models.RemoteError
import com.sharkaboi.appupdatechecker.models.VersionDetails
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.versions.DefaultStringVersionComparator
import com.sharkaboi.appupdatechecker.versions.VersionComparator
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class GithubTagSource(
    val ownerUsername: String,
    val repoName: String,
    val bearerToken: String? = null,
    override val currentVersion: String,
    override var versionComparator: VersionComparator<String> = DefaultStringVersionComparator,
) : AppUpdateCheckerSource<String>() {
    private val service =
        Retrofit.Builder()
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

        try {
            val response =
                if (!bearerToken.isNullOrBlank()) {
                    service.getLatestReleaseWithToken(
                        owner = ownerUsername,
                        repo = repoName,
                        authHeader = "Bearer $bearerToken",
                    )
                } else {
                    service.getLatestRelease(owner = ownerUsername, repo = repoName)
                }

            if (response.code() == 404) {
                throw PackageNotFoundException("Project not found in github with username $ownerUsername and repo $repoName")
            }
            val githubResponse =
                response.body() ?: throw RemoteError(Throwable(response.errorBody()?.string()))

            return VersionDetails(
                releaseNotes = githubResponse.body,
                latestVersionUrl = githubResponse.htmlUrl,
                latestVersion = githubResponse.tagName,
            )
        } catch (e: Exception) {
            if (e is AppUpdateCheckerException) {
                throw e
            }
            throw GenericError(e)
        }
    }
}
