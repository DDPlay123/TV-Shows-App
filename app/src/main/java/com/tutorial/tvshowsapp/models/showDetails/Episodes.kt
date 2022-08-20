package com.tutorial.tvshowsapp.models.showDetails

import com.google.gson.annotations.SerializedName

data class Episodes (
    @SerializedName("season")
    var season: String,

    @SerializedName("episode")
    var episode: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("air_date")
    var airDate: String
)