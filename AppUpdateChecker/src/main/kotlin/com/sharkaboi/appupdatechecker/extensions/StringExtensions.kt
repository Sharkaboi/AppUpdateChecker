package com.sharkaboi.appupdatechecker.extensions

internal fun String.isAfterVersion(other: String): Boolean {
    //Starts with v or V and followed by matched numbers and dots.
    val versionRegex = Regex("[v|V]\\d+(.\\d+)*")
    require(this.matches(versionRegex)) { "Current version tag is invalid" }
    require(other.matches(versionRegex)) { "Incoming version tag is invalid" }
    val thisVersion = this.trim().removePrefix("v").removePrefix("V")
    val otherVersion = other.trim().removePrefix("v").removePrefix("V")
    if (thisVersion == otherVersion) {
        return false
    }
    val thisParts = thisVersion.split(".").map { it.toLong() }
    val otherParts = otherVersion.split(".").map { it.toLong() }
    val length = thisParts.size.coerceAtLeast(otherParts.size)
    for (i in 0 until length) {
        val thisPart = if (i < thisParts.size) thisParts[i] else 0
        val otherPart = if (i < otherParts.size) otherParts[i] else 0
        if (thisPart == otherPart) continue
        if (thisPart < otherPart) return false
        if (thisPart > otherPart) return true
    }
    return false
}