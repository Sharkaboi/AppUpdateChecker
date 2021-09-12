package com.sharkaboi.appupdatechecker.extensions

import com.sharkaboi.appupdatechecker.models.AppUpdateCheckerSource
import okhttp3.HttpUrl

internal fun AppUpdateCheckerSource.GithubSource.isValid(): Boolean {
    return ownerUsername.isNotBlank() && repoName.isNotBlank()
}

internal fun AppUpdateCheckerSource.JsonSource.isValid(): Boolean {
    return HttpUrl.parse(jsonEndpoint) != null && jsonEndpoint.isNotBlank()
}

internal fun AppUpdateCheckerSource.XMLSource.isValid(): Boolean {
    return HttpUrl.parse(xmlEndpoint) != null && xmlEndpoint.isNotBlank()
}

internal fun AppUpdateCheckerSource.FDroidSource.isValid(): Boolean {
    return packageName != null && packageName.isNotBlank() && packageName.contains('.')
}
