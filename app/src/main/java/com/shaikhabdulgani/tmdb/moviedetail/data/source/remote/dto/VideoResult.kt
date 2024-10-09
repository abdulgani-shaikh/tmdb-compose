package com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResult(
    @SerialName("results")
    val results: List<VideoDto>
)