package com.sharkaboi.appupdatechecker.sources.xml

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

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
