package com.shaikhabdulgani.tmdb.search.data.remote.dto


import kotlinx.serialization.SerialName

data class SearchResultResponseDto(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<SearchResultDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)