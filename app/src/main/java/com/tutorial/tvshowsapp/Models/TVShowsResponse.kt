package com.tutorial.tvshowsapp.Models

import com.google.gson.annotations.SerializedName

data class TVShowsResponse (
    @SerializedName("page")
    var page: Int,

    @SerializedName("pages")
    var totalPages: Int,

    @SerializedName("tv_shows")
    var tvShows: MutableList<TVShows>
)