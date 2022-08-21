package com.tutorial.tvshowsapp.models.tvShows

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "tvShows")
data class TVShows (
    @PrimaryKey
    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("start_date")
    var startDate: String,

    @SerializedName("country")
    var country: String,

    @SerializedName("network")
    var network: String,

    @SerializedName("status")
    var status: String,

    @SerializedName("image_thumbnail_path")
    var thumbnail: String
): Serializable