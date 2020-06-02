/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bismeet.covidinfo.network


import com.bismeet.covidinfo.data.CountryNames
import com.bismeet.covidinfo.data.FilteredCountry
import com.bismeet.covidinfo.data.WorldSummary
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a devbyte playlist.
 */
interface AppService {
    @GET("timeline")
     fun getWorldSummaryAsync(): Deferred<WorldSummary>

    @GET("countries/{code}")
    fun getCountryDataFilteredAsync(@Path("code") code: String): Deferred<FilteredCountry>

    @GET("countries")
     fun getCountryListAsyncNewUrl(): Deferred<CountryNames>
}

/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
object CovidNetwork {
    //    var myclient:OkHttpClient=OkHttpClient.Builder().addInterceptor()
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofitInstance = Retrofit.Builder()
        .baseUrl("https://corona-api.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    val networkService: AppService = retrofitInstance.create(AppService::class.java)

}


