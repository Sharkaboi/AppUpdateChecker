package com.sharkaboi.appupdatechecker.sources.fdroid

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface FDroidService {
    @GET(FdroidConstants.PATH)
    suspend fun getReleases(
        @Path(FdroidConstants.PACKAGE_PATH_ID) packageName: String,
    ): Response<FDroidResponse>
}
