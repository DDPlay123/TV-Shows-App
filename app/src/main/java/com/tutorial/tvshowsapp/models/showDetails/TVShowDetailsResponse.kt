package com.tutorial.tvshowsapp.models.showDetails

import com.google.gson.annotations.SerializedName

data class TVShowDetailsResponse (
    @SerializedName("tvShow")
    var tvShowDetails: TVShowDetails
)