package com.tutorial.tvshowsapp.repositories

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tutorial.tvshowsapp.models.tvShows.TVShowsResponse
import com.tutorial.tvshowsapp.network.ApiClient
import com.tutorial.tvshowsapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTVShowRepository {

    companion object {
        val apiService: ApiService by lazy {
            ApiClient.getRetrofit()!!.create(ApiService::class.java)
        }
    }

    fun searchTVShow(query: String, page: Int): LiveData<TVShowsResponse> {
        val data: MutableLiveData<TVShowsResponse> = MutableLiveData()
        apiService.searchTVShow(query, page).enqueue(object : Callback<TVShowsResponse> {
            override fun onResponse(
                @NonNull call: Call<TVShowsResponse>,
                @NonNull response: Response<TVShowsResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(@NonNull call: Call<TVShowsResponse>, @NonNull t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}