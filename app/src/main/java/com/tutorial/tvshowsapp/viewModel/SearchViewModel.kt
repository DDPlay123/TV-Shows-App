package com.tutorial.tvshowsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tutorial.tvshowsapp.models.tvShows.TVShowsResponse
import com.tutorial.tvshowsapp.repositories.SearchTVShowRepository

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private val searchTVShowRepository: SearchTVShowRepository by lazy {
        SearchTVShowRepository()
    }

    fun searchTVShow(query: String, page: Int): LiveData<TVShowsResponse> =
        searchTVShowRepository.searchTVShow(query, page)
}