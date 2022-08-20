package com.tutorial.tvshowsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.network.ApiClient
import com.tutorial.tvshowsapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowDetailsRepository {

    companion object {
        val apiService: ApiService by lazy {
            ApiClient.getRetrofit()!!.create(ApiService::class.java)
        }
    }

    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> {
        val data: MutableLiveData<TVShowDetailsResponse> = MutableLiveData()
        apiService.getTVShowDetails(tvShowId).enqueue(object : Callback<TVShowDetailsResponse> {
            override fun onResponse(
                call: Call<TVShowDetailsResponse>,
                response: Response<TVShowDetailsResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TVShowDetailsResponse>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}