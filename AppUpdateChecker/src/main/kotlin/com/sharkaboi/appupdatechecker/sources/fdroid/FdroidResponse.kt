package com.sharkaboi.appupdatechecker.sources.fdroid


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class FdroidResponse(
    @Json(name = "packageName")
    val packageName: String,
    @Json(name = "packages")
    val packages: List<Package>
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class Package(
        @Json(name = "versionCode")
        val versionCode: Int,
        @Json(name = "versionName")
        val versionName: String
    )
}