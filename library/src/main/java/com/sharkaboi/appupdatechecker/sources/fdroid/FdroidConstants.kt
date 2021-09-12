package com.sharkaboi.appupdatechecker.sources.fdroid

internal object FdroidConstants {
    const val BASE_URL = "https://f-droid.org/api/v1/"
    const val PACKAGE_PATH_ID = "package"
    private const val PACKAGES_ROUTE = "packages/"
    const val HTML_BASE_URL = "https://f-droid.org/en/$PACKAGES_ROUTE"
    const val PATH = "$PACKAGES_ROUTE{$PACKAGE_PATH_ID}"
}
