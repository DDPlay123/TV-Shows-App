package com.tutorial.tvshowsapp.models.showDetails

import com.google.gson.annotations.SerializedName

data class TVShowDetails (
    @SerializedName("url")
    var url: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("runtime")
    var runtime: String,

    @SerializedName("image_path")
    var imagePath: String,

    @SerializedName("rating")
    var rating: String,

    @SerializedName("genres")
    var genres: List<String>,

    @SerializedName("pictures")
    var pictures: List<String>,

    @SerializedName("episodes")
    var episodes: List<Episodes>
)