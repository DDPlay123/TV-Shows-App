package com.tutorial.tvshowsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tutorial.tvshowsapp.models.tvShows.TVShowsResponse
import com.tutorial.tvshowsapp.repositories.MostPopularTVShowsRepository

class MostPopularTVShowsViewModel(application: Application): AndroidViewModel(application) {

    private val mostPopularTVShowsRepository: MostPopularTVShowsRepository by lazy {
        MostPopularTVShowsRepository()
    }

    fun getMostPopularTVShows(page: Int): LiveData<TVShowsResponse> =
        mostPopularTVShowsRepository.getMostPopularTVShows(page)
}