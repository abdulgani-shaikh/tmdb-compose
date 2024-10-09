package com.shaikhabdulgani.tmdb.moviedetail.data.source.remote

import com.shaikhabdulgani.tmdb.global.Constants
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.dto.MovieDetailDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

interface MovieDetailApi {
    suspend fun getMovieDetail(
        id: Int,
        mediaType: String,
        append_to_response: String = "videos,credits,recommendations"
    ): MovieDetailDto
}

class MovieDetailApiImpl (
    private val client: HttpClient
) : MovieDetailApi {
    override suspend fun getMovieDetail(
        id: Int,
        mediaType: String,
        append_to_response: String
    ): MovieDetailDto {
        return client.get {
            url("${Constants.BASE_URL}${mediaType}/${id}")
            parameter("append_to_response",append_to_response)
        }.body()
    }
}