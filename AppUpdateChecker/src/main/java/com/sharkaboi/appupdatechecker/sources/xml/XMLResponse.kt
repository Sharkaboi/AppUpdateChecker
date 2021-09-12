package com.sharkaboi.appupdatechecker.sources.xml

import androidx.annotation.Keep
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Keep
@Root(name = "version")
data class XMLResponse(
    @field:Element(name = "latestVersion")
    @param:Element(name = "latestVersion")
    val latestVersion: String,
    @field:Element(name = "latestVersionUrl")
    @param:Element(name = "latestVersionUrl")
    val latestVersionUrl: String,
    @field:Element(name = "releaseNotes")
    @param:Element(name = "releaseNotes")
    val releaseNotes: String?
)
