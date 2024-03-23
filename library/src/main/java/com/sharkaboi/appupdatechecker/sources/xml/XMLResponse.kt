package com.sharkaboi.appupdatechecker.sources.xml

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "version")
data class XMLResponse(
    @field:Element(name = "latestVersionName", required = false)
    @param:Element(name = "latestVersionName", required = false)
    val latestVersionName: String?,
    @field:Element(name = "latestVersionCode", required = false)
    @param:Element(name = "latestVersionCode", required = false)
    val latestVersionCode: Long?,
    @field:Element(name = "latestVersionUrl")
    @param:Element(name = "latestVersionUrl")
    val latestVersionUrl: String,
    @field:Element(name = "releaseNotes")
    @param:Element(name = "releaseNotes")
    val releaseNotes: String?
)
