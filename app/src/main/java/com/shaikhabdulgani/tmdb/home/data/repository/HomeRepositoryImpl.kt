package com.shaikhabdulgani.tmdb.home.data.repository

import android.util.Log
import com.shaikhabdulgani.tmdb.base.BaseRepository
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.data.util.getRank
import com.shaikhabdulgani.tmdb.core.data.util.isOneHourEarlier
import com.shaikhabdulgani.tmdb.home.data.local.dao.MovieDao
import com.shaikhabdulgani.tmdb.home.data.local.dao.SeriesDao
import com.shaikhabdulgani.tmdb.home.data.mapper.toEntity
import com.shaikhabdulgani.tmdb.home.data.mapper.toMovie
import com.shaikhabdulgani.tmdb.home.data.remote.HomeApi
import com.shaikhabdulgani.tmdb.home.data.remote.model.MovieListType
import com.shaikhabdulgani.tmdb.home.data.remote.model.SeriesListType
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import com.shaikhabdulgani.tmdb.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val api: HomeApi,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao,
) : HomeRepository, BaseRepository() {

    companion object {
        private const val TAG = "HomeRepositoryImpl"
    }

    override suspend fun getTrendingMovies(page: Int): Result<List<Media>> {
        return execute {
            val listType = MovieListType.POPULAR.value
            getMovieList(page, listType)
        }
    }

    override suspend fun getUpcomingMovies(page: Int): Result<List<Media>> {
        return execute {
            val listType = MovieListType.UPCOMING.value
            getMovieList(page, listType)
        }
    }

    override suspend fun getPopularSeries(page: Int): Result<List<Media>> {
        return execute {
            val listType = SeriesListType.POPULAR.value
            getSeriesList(page, listType)
        }
    }

    override suspend fun getOnTheAirSeries(page: Int): Result<List<Media>> {
        return execute {
            val listType = SeriesListType.ON_THE_AIR.value
            getSeriesList(page, listType)
        }
    }

    private suspend fun getSeriesList(
        page: Int,
        listType: String,
        forceRemote: Boolean = false
    ): List<Media> {
        //fetching data from local db
        if (!forceRemote) {
            val localResult = seriesDao.getSeries(
                category = listType,
                offset = (page - 1) * pageSize,
                limit = pageSize,
            )

            //returning if available
            if (localResult.isNotEmpty()) {
                if (!isOneHourEarlier(localResult[0].timestamp) || page != 1)
                    return localResult.map { it.toMovie() }
                seriesDao.clear()
            }
        } else {
            if (page == 1) {
                seriesDao.clear()
            }
        }

        Log.d(TAG, "Data not available locally")
        //data not available in local db
        val result = api.getSeries(
            page = page,
            category = listType
        ).results

        val seriesEntityList = result.mapIndexed { i, it ->
            it.toEntity(listType, getRank(page, pageSize, i))
        }

        seriesDao.insertAll(seriesEntityList)

        return seriesEntityList.map { it.toMovie() }
    }

    private suspend fun getMovieList(
        page: Int,
        listType: String,
        forceRemote: Boolean = false
    ): List<Media> {
        //fetching data from local db
        if (!forceRemote) {
            val localResult = movieDao.getMovies(
                category = listType,
                offset = (page - 1) * pageSize,
                limit = pageSize,
            )

            //returning if available
            if (localResult.isNotEmpty()) {
                if (!isOneHourEarlier(localResult[0].timestamp) || page != 1) {
                    //data available in local data
                    Log.d(TAG, "data available in local data")
                    return localResult.map { it.toMovie() }
                }
                movieDao.clear()
            }
        } else {
            if (page == 1) {
                seriesDao.clear()
            }
        }

        Log.d(TAG, "Data not available locally")
        //data not available in local db
        val result = api.getMovies(
            page = page,
            category = listType
        ).results

        val movieEntityList = result.mapIndexed { i, it ->
            it.toEntity(listType, getRank(page, pageSize, i))
        }

        movieDao.insertAll(movieEntityList)

        return movieEntityList.map { it.toMovie() }
    }
}