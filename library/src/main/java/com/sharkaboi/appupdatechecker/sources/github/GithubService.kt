package com.sharkaboi.appupdatechecker.sources.github

import retrofit2.http.GET
import retrofit2.http.Path

internal interface GithubService {

    @GET(GithubConstants.PATH)
    suspend fun getLatestRelease(
        @Path(GithubConstants.OWNER_PATH_ID) owner: String,
        @Path(GithubConstants.REPO_PATH_ID) repo: String,
    ): GithubResponse
}
