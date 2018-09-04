package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    /**
     * Retrofit Client for all our api calls to unsplash.com
     */
    val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    val retrofit= Retrofit.Builder()
            .baseUrl(PhotoConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}