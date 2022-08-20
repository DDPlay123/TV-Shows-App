package com.tutorial.tvshowsapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tutorial.tvshowsapp.models.TVShowsResponse
import com.tutorial.tvshowsapp.Repositiories.MostPopularTVShowsRepository

class MostPopularTVShowsViewModel(application: Application): AndroidViewModel(application) {

    private val mostPopularTVShowsRepository: MostPopularTVShowsRepository by lazy {
        MostPopularTVShowsRepository()
    }

    fun getMostPopularTVShows(page: Int): LiveData<TVShowsResponse> {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page)
    }
}