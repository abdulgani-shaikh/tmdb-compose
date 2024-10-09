package com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationsResultDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<RecommendationDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)