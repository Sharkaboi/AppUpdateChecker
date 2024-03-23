package com.sharkaboi.appupdatechecker.sources.xml

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

interface XMLService {
    @GET
    suspend fun getXMLReleaseMetaDataAsync(
        @Url url: String
    ): XMLResponse
}
