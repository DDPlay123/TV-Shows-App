package com.tutorial.tvshowsapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tutorial.tvshowsapp.models.tvShows.TVShows

@Database(entities = [TVShows::class], version = 1, exportSchema = false)
abstract class TVShowsDatabase: RoomDatabase() {
    abstract fun tvShowsDao(): TVShowsDao
}