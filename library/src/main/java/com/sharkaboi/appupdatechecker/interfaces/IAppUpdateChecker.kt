package com.sharkaboi.appupdatechecker.interfaces

import com.sharkaboi.appupdatechecker.models.UpdateState
import kotlinx.coroutines.Deferred

internal interface IAppUpdateChecker {
    suspend fun checkUpdate(): UpdateState
    suspend fun checkUpdateAsync(): Deferred<UpdateState>
}
