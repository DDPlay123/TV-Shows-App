package com.tutorial.tvshowsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.repositories.TVShowDetailsRepository
import com.tutorial.tvshowsapp.room.TVShowsDatabase
import com.tutorial.tvshowsapp.utilities.TVShows_Database
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class TVShowDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val tVShowDetailsRepository: TVShowDetailsRepository by lazy {
        TVShowDetailsRepository()
    }

    private val tvShowsDatabase: TVShowsDatabase by lazy {
        Room.databaseBuilder(application, TVShowsDatabase::class.java, TVShows_Database)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun closeDatabase() =
        tvShowsDatabase.close()

    fun getTVShowDetails(tvShowId: String): LiveData<TVShowDetailsResponse> =
        tVShowDetailsRepository.getTVShowDetails(tvShowId)

    fun addToWatchList(tvShows: TVShows): Completable =
        tvShowsDatabase.tvShowsDao().addToWatchList(tvShows)

    fun getTVShowFromWatchList(tvShowId: String): Flowable<TVShows> =
        tvShowsDatabase.tvShowsDao().getTVShowFromWatchList(tvShowId)

    fun removeTVShowFromWatchList(tvShows: TVShows): Completable =
        tvShowsDatabase.tvShowsDao().removeFromWatchList(tvShows)
}