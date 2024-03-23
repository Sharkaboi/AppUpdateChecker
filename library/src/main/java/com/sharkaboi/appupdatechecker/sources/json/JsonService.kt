package com.sharkaboi.appupdatechecker.sources.json

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

internal interface JsonService {
    @GET
    suspend fun getJsonReleaseMetaDataAsync(
        @Url url: String
    ): JsonResponse
}
