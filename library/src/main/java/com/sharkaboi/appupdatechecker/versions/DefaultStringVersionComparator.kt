package com.sharkaboi.appupdatechecker.versions

import com.sharkaboi.appupdatechecker.models.InvalidVersionException
import kotlin.math.max

object DefaultStringVersionComparator : VersionComparator<String> {
    override fun isNewerVersion(currentVersion: String, newVersion: String): Boolean {
        val currentVersionSubParts = parseVersion(currentVersion)
        val newVersionSubParts = parseVersion(newVersion)
        val length = max(currentVersionSubParts.size, newVersionSubParts.size)

        val paddedCurrentVersionParts = padUntil(currentVersionSubParts, length)
        val paddedNewVersionParts = padUntil(newVersionSubParts, length)

        if (paddedCurrentVersionParts == paddedNewVersionParts) return false

        for (i in 0 until length) {
            val newVersionPart = paddedNewVersionParts[i]
            val currentVersionPart = paddedCurrentVersionParts[i]
            if (newVersionPart == currentVersionPart) continue

            return newVersionPart > currentVersionPart
        }

        return false
    }

    private fun padUntil(list: List<Long>, length: Int): List<Long> {
        return list + List(length - list.size) { 0L }
    }

    private fun parseVersion(version: String): List<Long> {
        return version
            .trimStart('v', 'V', ' ')
            .trimEnd(' ')
            .split('.')
            .map { it.toLongOrNull() ?: throw InvalidVersionException("Invalid version $version") }
    }
}