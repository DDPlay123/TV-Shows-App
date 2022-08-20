package com.tutorial.tvshowsapp.adapter.tvShows

import com.tutorial.tvshowsapp.models.tvShows.TVShows

interface TVShowsListener {
    fun onTVShowClicked(tvShows: TVShows)
}