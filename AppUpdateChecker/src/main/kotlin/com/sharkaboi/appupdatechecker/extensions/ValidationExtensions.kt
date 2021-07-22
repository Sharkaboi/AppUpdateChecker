package com.sharkaboi.appupdatechecker.extensions

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource

internal fun AppUpdateCheckerSource.GithubSource.isValid(): Boolean {
    return ownerUsername.isNotBlank() and repoName.isNotBlank()
}