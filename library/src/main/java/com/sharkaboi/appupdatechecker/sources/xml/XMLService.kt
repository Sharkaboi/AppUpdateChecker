package com.sharkaboi.appupdatechecker.sources.xml

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

interface XMLService {
    @GET
    fun getXMLReleaseMetaDataAsync(
        @Url url: String
    ): Deferred<XMLResponse>
}
