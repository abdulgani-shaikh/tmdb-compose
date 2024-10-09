package com.shaikhabdulgani.tmdb.home.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetSeriesListResponseModel(
    val page: Int,
    val results: List<SeriesDto>
)