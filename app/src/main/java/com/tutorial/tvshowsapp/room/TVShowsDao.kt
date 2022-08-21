package com.tutorial.tvshowsapp.room

import androidx.room.*
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TVShowsDao {

    @Query("SELECT * FROM tvShows")
    fun getWatchList(): Flowable<MutableList<TVShows>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToWatchList(tvShow: TVShows): Completable

    @Delete
    fun removeFromWatchList(tvShow: TVShows)
}