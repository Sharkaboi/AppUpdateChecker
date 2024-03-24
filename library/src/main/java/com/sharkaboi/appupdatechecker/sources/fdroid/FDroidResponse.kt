package com.sharkaboi.appupdatechecker.sources.fdroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FDroidResponse(
    @Json(name = "packageName")
    val packageName: String,
    @Json(name = "packages")
    val packages: List<Package>,
) {
    @JsonClass(generateAdapter = true)
    data class Package(
        @Json(name = "versionCode")
        val versionCode: Long,
        @Json(name = "versionName")
        val versionName: String,
    )
}
