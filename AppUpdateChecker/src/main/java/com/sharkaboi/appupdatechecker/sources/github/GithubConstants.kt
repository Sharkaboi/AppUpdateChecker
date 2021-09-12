package com.sharkaboi.appupdatechecker.sources.github

internal object GithubConstants {
    const val BASE_URL = "https://api.github.com"
    const val OWNER_PATH_ID = "owner"
    const val REPO_PATH_ID = "repo"
    const val PATH = "/repos/{$OWNER_PATH_ID}/{$REPO_PATH_ID}/releases/latest"
}
