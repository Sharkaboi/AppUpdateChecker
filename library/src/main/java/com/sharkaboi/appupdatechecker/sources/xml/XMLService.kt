package com.sharkaboi.appupdatechecker.sources.xml

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface XMLService {
    @GET
    suspend fun getXMLReleaseMetaData(
        @Url url: String,
    ): Response<XMLResponse>
}
