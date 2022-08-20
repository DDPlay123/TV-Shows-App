package com.tutorial.tvshowsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit? {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl("https://www.episodate.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }
}