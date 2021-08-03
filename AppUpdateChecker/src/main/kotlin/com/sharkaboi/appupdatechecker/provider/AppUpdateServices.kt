package com.sharkaboi.appupdatechecker.provider

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sharkaboi.appupdatechecker.sources.fdroid.FdroidConstants
import com.sharkaboi.appupdatechecker.sources.fdroid.FdroidService
import com.sharkaboi.appupdatechecker.sources.github.GithubConstants
import com.sharkaboi.appupdatechecker.sources.github.GithubService
import com.sharkaboi.appupdatechecker.sources.json.JsonConstants
import com.sharkaboi.appupdatechecker.sources.json.JsonService
import com.sharkaboi.appupdatechecker.sources.xml.XMLConstants
import com.sharkaboi.appupdatechecker.sources.xml.XMLService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

internal object AppUpdateServices {

    val fDroidService: FdroidService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(FdroidConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FdroidService::class.java)
    }

    val jsonService: JsonService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(JsonConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(JsonService::class.java)
    }

    val xmlService: XMLService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(XMLConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(XMLService::class.java)
    }

    val githubService: GithubService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(GithubConstants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubService::class.java)
    }
}