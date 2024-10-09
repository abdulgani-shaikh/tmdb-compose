package com.shaikhabdulgani.tmdb.home.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetMovieListResponseModel(
    val page: Int,
    val results: List<MovieDto>
)