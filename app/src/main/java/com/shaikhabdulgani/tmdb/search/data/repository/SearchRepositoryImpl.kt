package com.shaikhabdulgani.tmdb.search.data.repository

import com.shaikhabdulgani.tmdb.base.BaseRepository
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.search.data.mapper.toMovieSearchResult
import com.shaikhabdulgani.tmdb.search.data.mapper.toPersonSearchResult
import com.shaikhabdulgani.tmdb.search.data.mapper.toSearchResult
import com.shaikhabdulgani.tmdb.search.data.remote.SearchApi
import com.shaikhabdulgani.tmdb.search.domain.model.SearchResult
import com.shaikhabdulgani.tmdb.search.domain.model.SearchType
import com.shaikhabdulgani.tmdb.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: SearchApi
) : SearchRepository, BaseRepository() {
    override suspend fun search(
        query: String,
        page: Int,
        searchType: SearchType
    ): Result<List<SearchResult>> {
        return execute {
            val result = api.search(
                query = query,
                page = page,
                searchType = searchType.getValue()).results
            when(searchType){
                SearchType.MOVIE -> result.map { it.toMovieSearchResult() }
                SearchType.SERIES -> result.map { it.toSearchResult() }
                SearchType.PERSON -> result.map { it.toPersonSearchResult() }
                SearchType.ALL -> result.map { it.toSearchResult() }
            }
        }
    }
}