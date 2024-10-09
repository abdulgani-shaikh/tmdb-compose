package com.shaikhabdulgani.tmdb.home.data.remote

import com.shaikhabdulgani.tmdb.global.Constants
import com.shaikhabdulgani.tmdb.home.data.remote.model.GetMovieListResponseModel
import com.shaikhabdulgani.tmdb.home.data.remote.model.GetSeriesListResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

interface HomeApi {

    suspend fun getMovies(
        category: String,
        page: Int,
    ): GetMovieListResponseModel

    suspend fun getSeries(
        category: String,
        page: Int,
    ): GetSeriesListResponseModel
}

class HomeApiImpl (
    private val client: HttpClient
) : HomeApi {

    override suspend fun getMovies(category: String, page: Int): GetMovieListResponseModel {
        return client.get {
            url("${Constants.BASE_URL}movie/${category}")
            parameter("page", page)
        }.body()
    }

    override suspend fun getSeries(category: String, page: Int): GetSeriesListResponseModel {
        return client.get {
            url("${Constants.BASE_URL}tv/${category}")
            parameter("page", page)
        }.body()
    }
}