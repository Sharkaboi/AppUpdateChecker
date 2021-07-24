package com.sharkaboi.appupdatechecker.sources.fdroid

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

internal interface FdroidService {

    @GET(FdroidConstants.PATH)
    fun getReleasesAsync(
        @Path(FdroidConstants.PACKAGE_PATH_ID) packageName: String
    ): Deferred<FdroidResponse>
}