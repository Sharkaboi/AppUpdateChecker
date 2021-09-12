package com.sharkaboi.appupdatechecker

import android.content.Context
import com.sharkaboi.appupdatechecker.extensions.*
import com.sharkaboi.appupdatechecker.interfaces.IAppUpdateChecker
import com.sharkaboi.appupdatechecker.mappers.toUpdateAvailableState
import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import com.sharkaboi.appupdatechecker.models.UpdateState
import com.sharkaboi.appupdatechecker.provider.AppUpdateServices
import com.sharkaboi.appupdatechecker.sources.fdroid.isAfterVersion
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
                require(currentVersionTag.matches(versionRegex)) { "Invalid current version tag" }
                return@async when (source) {
                    is AppUpdateCheckerSource.FDroidSource -> handleFDroidCheck()
                    is AppUpdateCheckerSource.GithubSource -> handleGithubCheck()
                    is AppUpdateCheckerSource.JsonSource -> handleJsonCheck()
                    is AppUpdateCheckerSource.XMLSource -> handleXmlCheck()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@async UpdateState.GenericError(e)
            }
        }
    }

    private suspend fun handleFDroidCheck(): UpdateState {
        require(source is AppUpdateCheckerSource.FDroidSource) { "Invalid source" }
        val packageName = source.packageName ?: context.packageName
        val modifiedSource = source.copy(
            packageName = packageName
        )
        if (modifiedSource.isValid()) {
            return try {
                val release = AppUpdateServices.fDroidService.getReleasesAsync(
                    packageName = packageName
                ).await()
                if (release.isAfterVersion(currentVersionTag)) {
                    release.toUpdateAvailableState(modifiedSource)
                } else {
                    UpdateState.LatestVersionInstalled
                }
            } catch (e: Exception) {
                e.printStackTrace()
                UpdateState.FDroidInvalid
            }
        } else {
            return UpdateState.FDroidMalformed
        }
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
                    release.toUpdateAvailableState(source)
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

    private suspend fun handleJsonCheck(): UpdateState {
        require(source is AppUpdateCheckerSource.JsonSource) { "Invalid source" }
        if (source.isValid()) {
            return try {
                val release = AppUpdateServices.jsonService.getJsonReleaseMetaDataAsync(
                    url = source.jsonEndpoint
                ).await()
                if (release.latestVersion.isAfterVersion(currentVersionTag)) {
                    release.toUpdateAvailableState(source)
                } else {
                    UpdateState.LatestVersionInstalled
                }
            } catch (e: Exception) {
                e.printStackTrace()
                UpdateState.JSONInvalid
            }
        } else {
            return UpdateState.JSONMalformed
        }
    }

    private suspend fun handleXmlCheck(): UpdateState {
        require(source is AppUpdateCheckerSource.XMLSource) { "Invalid source" }
        if (source.isValid()) {
            return try {
                val release = AppUpdateServices.xmlService.getXMLReleaseMetaDataAsync(
                    url = source.xmlEndpoint
                ).await()
                if (release.latestVersion.isAfterVersion(currentVersionTag)) {
                    release.toUpdateAvailableState(source)
                } else {
                    UpdateState.LatestVersionInstalled
                }
            } catch (e: Exception) {
                e.printStackTrace()
                UpdateState.XMLInvalid
            }
        } else {
            return UpdateState.XMLMalformed
        }
    }
}
