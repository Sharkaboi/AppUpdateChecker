package com.sharkaboi.appupdatechecker.sources.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonResponse(
    @Json(name = "latestVersionName")
    val latestVersionName: String?,
    @Json(name = "latestVersionCode")
    val latestVersionCode: Long?,
    @Json(name = "latestVersionUrl")
    val latestVersionUrl: String,
    @Json(name = "releaseNotes")
    val releaseNotes: String?,
)
