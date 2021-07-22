package com.sharkaboi.appupdatechecker.sources.json

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

internal interface JsonService {
    @GET
    fun getJsonReleaseMetaDataAsync(
        @Url url: String
    ): Deferred<JsonResponse>
}