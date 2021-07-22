package com.sharkaboi.appupdatechecker.provider

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sharkaboi.appupdatechecker.sources.github.GithubConstants
import com.sharkaboi.appupdatechecker.sources.github.GithubService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object AppUpdateServices {
    val githubService: GithubService by lazy {
        Retrofit.Builder()
            .baseUrl(GithubConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubService::class.java)
    }
}