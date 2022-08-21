package com.tutorial.tvshowsapp.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.tutorial.tvshowsapp.room.TVShowsDatabase
import com.tutorial.tvshowsapp.utilities.TVShows_Database

abstract class BaseActivity: AppCompatActivity() {

    private var tvShowsDatabase: TVShowsDatabase? = null

    fun initTVShowDatabase(context: Context): TVShowsDatabase? {
        tvShowsDatabase = Room.databaseBuilder(context, TVShowsDatabase::class.java, TVShows_Database)
            .fallbackToDestructiveMigration()
            .build()

        return tvShowsDatabase
    }

    private fun closeDatabase() {
        tvShowsDatabase?.close()
        tvShowsDatabase = null
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    override fun onStop() {
        super.onStop()
//        closeDatabase()
    }

    override fun onDestroy() {
        super.onDestroy()
//        closeDatabase()
    }
}