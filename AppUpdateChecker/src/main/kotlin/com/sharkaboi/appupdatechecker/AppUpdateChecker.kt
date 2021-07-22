package com.sharkaboi.appupdatechecker

import android.content.Context
import com.sharkaboi.appupdatechecker.extensions.installedVersionTag
import com.sharkaboi.appupdatechecker.extensions.isAfterVersion
import com.sharkaboi.appupdatechecker.extensions.isInternetConnected
import com.sharkaboi.appupdatechecker.extensions.isValid
import com.sharkaboi.appupdatechecker.interfaces.IAppUpdateChecker
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import com.sharkaboi.appupdatechecker.provider.AppUpdateServices
import kotlinx.coroutines.*

class AppUpdateChecker(
    private val context: Context,
    private val source: AppUpdateCheckerSource,
    private val currentVersionTag: String = context.installedVersionTag
) : IAppUpdateChecker {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun checkUpdate(): UpdateState = withContext(dispatcher) {
        return@withContext checkUpdateAsync().await()
    }

    override suspend fun checkUpdateAsync(): Deferred<UpdateState> = withContext(dispatcher) {
        return@withContext async {
            try {
                if (!context.isInternetConnected) {
                    return@async UpdateState.NoNetworkFound
                }
                return@async when (source) {
                    AppUpdateCheckerSource.AmazonSource -> handleAmazonCheck()
                    AppUpdateCheckerSource.FDroidSource -> handleFDroidCheck()
                    is AppUpdateCheckerSource.GithubSource -> handleGithubCheck()
                    is AppUpdateCheckerSource.GitlabSource -> handleGitlabCheck()
                    AppUpdateCheckerSource.GooglePlaySource -> handleGooglePlayCheck()
                    is AppUpdateCheckerSource.JsonSource -> handleJsonCheck()
                    is AppUpdateCheckerSource.XMLSource -> handleXmlCheck()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@async UpdateState.GenericUpdateState(e)
            }
        }
    }

    private suspend fun handleAmazonCheck(): UpdateState {
        TODO("Not yet implemented")
    }

    private suspend fun handleFDroidCheck(): UpdateState {
        TODO("Not yet implemented")
    }

    private suspend fun handleGithubCheck(): UpdateState {
        require(source is AppUpdateCheckerSource.GithubSource) { "Invalid source" }
        if (source.isValid()) {
            return try {
                val release = AppUpdateServices.githubService.getLatestReleaseAsync(
                    owner = source.ownerUsername,
                    repo = source.repoName
                ).await()
                if (release.tagName.isAfterVersion(currentVersionTag)) {
                    UpdateState.UpdateAvailable(
                        latestVersion = release.tagName,
                        latestVersionUrl = release.htmlUrl,
                        releaseNotes = release.body,
                        source = source
                    )
                } else {
                    UpdateState.LatestVersionInstalled
                }
            } catch (e: Exception) {
                e.printStackTrace()
                UpdateState.GithubInvalid
            }
        } else {
            return UpdateState.GithubMalformed
        }
    }

    private suspend fun handleGitlabCheck(): UpdateState {
        TODO("Not yet implemented")
    }

    private suspend fun handleGooglePlayCheck(): UpdateState {
        TODO("Not yet implemented")
    }

    private suspend fun handleJsonCheck(): UpdateState {
        TODO("Not yet implemented")
    }

    private suspend fun handleXmlCheck(): UpdateState {
        TODO("Not yet implemented")
    }
}
