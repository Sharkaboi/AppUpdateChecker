package com.sharkaboi.appupdatechecker.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

internal val Context.isInternetConnected: Boolean
    get() {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.let { cm ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.activeNetwork?.let { an ->
                    val networkCapabilities = cm.getNetworkCapabilities(an)
                    networkCapabilities?.let { nc ->
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            } else {
                val netInfo = cm.activeNetworkInfo
                netInfo != null && netInfo.isConnectedOrConnecting
            }
        } ?: false
    }

internal val Context.installedVersionTag: String
    get() {
        return this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
