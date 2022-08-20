package com.tutorial.tvshowsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.repositories.TVShowDetailsRepository

class TVShowDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val tVShowDetailsRepository: TVShowDetailsRepository by lazy {
        TVShowDetailsRepository()
    }

    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> {
        return tVShowDetailsRepository.getTVShowDetails(tvShowId)
    }
}