package com.sharkaboi.appupdatechecker.sources.json

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

internal interface JsonService {
    @GET
    suspend fun getJsonReleaseMetaData(
        @Url url: String
    ): Response<JsonResponse>
}
