package com.sharkaboi.appupdatechecker.sources.github

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class GithubResponse(
    @Json(name = "body")
    val body: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "tag_name")
    val tagName: String
)
