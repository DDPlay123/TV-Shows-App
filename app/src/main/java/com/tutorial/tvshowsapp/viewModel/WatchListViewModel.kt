package com.tutorial.tvshowsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.room.TVShowsDatabase
import com.tutorial.tvshowsapp.utilities.TVShows_Database
import io.reactivex.rxjava3.core.Flowable

class WatchListViewModel(application: Application): AndroidViewModel(application) {

    private val tvShowsDatabase: TVShowsDatabase by lazy {
        Room.databaseBuilder(application, TVShowsDatabase::class.java, TVShows_Database)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun loadWatchList(): Flowable<MutableList<TVShows>> =
        tvShowsDatabase.tvShowsDao().getWatchList()
}