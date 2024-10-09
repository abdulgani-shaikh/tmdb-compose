package com.shaikhabdulgani.tmdb.search.data.remote

import com.shaikhabdulgani.tmdb.global.Constants
import com.shaikhabdulgani.tmdb.search.data.remote.dto.SearchResultResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

interface SearchApi {

    suspend fun search(
        searchType: String,
        query: String,
        page: Int,
    ): SearchResultResponseDto

}

class SearchApiImpl (
    private val client: HttpClient
) : SearchApi {

    override suspend fun search(
        searchType: String,
        query: String,
        page: Int
    ): SearchResultResponseDto {
        return client.get {
            url("${Constants.BASE_URL}search/${searchType}")
            parameter("query", query)
            parameter("page", page)
        }.body()
    }

}