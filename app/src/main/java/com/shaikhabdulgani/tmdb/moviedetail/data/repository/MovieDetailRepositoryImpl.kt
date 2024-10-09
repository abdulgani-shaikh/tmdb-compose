package com.shaikhabdulgani.tmdb.moviedetail.data.repository

import com.shaikhabdulgani.tmdb.base.BaseRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.moviedetail.data.mapper.toMovieDetail
import com.shaikhabdulgani.tmdb.moviedetail.data.mapper.toMovieDetailEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.MovieDetailDao
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.MovieDetailApi
import com.shaikhabdulgani.tmdb.moviedetail.domain.model.MovieDetail
import com.shaikhabdulgani.tmdb.moviedetail.domain.repository.MovieDetailRepository
import com.shaikhabdulgani.tmdb.search.domain.model.ContentType
import kotlinx.coroutines.flow.Flow

class MovieDetailRepositoryImpl(
    private val movieDetailApi: MovieDetailApi,
    private val movieDetailDao: MovieDetailDao
) : BaseRepository(), MovieDetailRepository {

    override suspend fun getMovieDetail(
        id: Int,
        contentType: ContentType
    ): Flow<Resource<MovieDetail>> {
        return executeWithFlow {

            val result = movieDetailDao.getMovieDetail(id = id,mediaType = contentType.getValue())
            if (result != null) {
                return@executeWithFlow result.toMovieDetail()
            }

            val localDb = movieDetailApi.getMovieDetail(id = id,mediaType = contentType.getValue()).toMovieDetailEntity(contentType.getValue())
            movieDetailDao.insert(localDb)

            return@executeWithFlow localDb.toMovieDetail()
        }
    }
}