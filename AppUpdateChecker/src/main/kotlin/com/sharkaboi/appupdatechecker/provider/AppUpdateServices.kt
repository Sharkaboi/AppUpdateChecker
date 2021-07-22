package com.sharkaboi.appupdatechecker.provider

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sharkaboi.appupdatechecker.sources.github.GithubConstants
import com.sharkaboi.appupdatechecker.sources.github.GithubService
import com.sharkaboi.appupdatechecker.sources.json.JsonConstants
import com.sharkaboi.appupdatechecker.sources.json.JsonService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object AppUpdateServices {

    val jsonService: JsonService by lazy {
        Retrofit.Builder()
            .baseUrl(JsonConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(JsonService::class.java)
    }

    val githubService: GithubService by lazy {
        Retrofit.Builder()
            .baseUrl(GithubConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubService::class.java)
    }
}