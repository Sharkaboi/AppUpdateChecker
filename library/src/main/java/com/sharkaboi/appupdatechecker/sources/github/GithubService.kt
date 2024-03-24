package com.sharkaboi.appupdatechecker.sources.github

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

internal interface GithubService {

    @GET(GithubConstants.PATH)
    suspend fun getLatestRelease(
        @Path(GithubConstants.OWNER_PATH_ID) owner: String,
        @Path(GithubConstants.REPO_PATH_ID) repo: String,
    ): Response<GithubResponse>

    @GET(GithubConstants.PATH)
    suspend fun getLatestReleaseWithToken(
        @Path(GithubConstants.OWNER_PATH_ID) owner: String,
        @Path(GithubConstants.REPO_PATH_ID) repo: String,
        @Header("Authorization") authHeader: String,
    ): Response<GithubResponse>
}
