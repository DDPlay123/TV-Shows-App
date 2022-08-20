package com.tutorial.tvshowsapp.network

import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.models.tvShows.TVShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("most-popular")
    fun getMostPopularTVShows(@Query("page") page: Int): Call<TVShowsResponse>

    @GET("show-details")
    fun getTVShowDetails(@Query("q") tvShowId: String): Call<TVShowDetailsResponse>
}