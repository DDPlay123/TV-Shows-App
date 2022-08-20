package com.tutorial.tvshowsapp.Network

import com.tutorial.tvshowsapp.Models.TVShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("most-popular")
    fun getMostPopularTVShows(@Query("page") page: Int): Call<TVShowsResponse>
}