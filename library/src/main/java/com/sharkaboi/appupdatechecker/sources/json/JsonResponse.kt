package com.sharkaboi.appupdatechecker.sources.json

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class JsonResponse(
    @Json(name = "latestVersion")
    val latestVersion: String,
    @Json(name = "latestVersionUrl")
    val latestVersionUrl: String,
    @Json(name = "releaseNotes")
    val releaseNotes: String?
)
