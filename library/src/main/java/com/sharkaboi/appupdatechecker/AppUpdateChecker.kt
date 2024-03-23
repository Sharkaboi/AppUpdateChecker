package com.sharkaboi.appupdatechecker

import com.sharkaboi.appupdatechecker.models.UpdateResult
import com.sharkaboi.appupdatechecker.sources.AppUpdateCheckerSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class AppUpdateChecker<T>(private val source: AppUpdateCheckerSource<T>) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun checkUpdate(): UpdateResult = withContext(dispatcher) {
        return@withContext checkUpdateAsync().await()
    }

    suspend fun checkUpdateAsync(): Deferred<UpdateResult> = withContext(dispatcher) {
        return@withContext async {
            return@async source.getUpdateState()
        }
    }
}
