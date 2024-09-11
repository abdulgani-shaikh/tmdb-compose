package com.shaikhabdulgani.tmdb.core.domain.repository

import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(uid: String, forceRemoteFetch: Boolean = false): Result<User>
    suspend fun addUser(uid: String, username: String, email: String): Result<User>
    suspend fun bookmarkMovie(uid: String,movieId: String): Result<Boolean>
    suspend fun removeMovieBookmark(uid: String,movieId: String): Result<Boolean>
    suspend fun isBookmarked(uid: String, movieId: String): Result<Boolean>
    suspend fun getFavorites(uid: String, forceRemoteFetch: Boolean = false): Flow<Resource<List<Media>>>
    suspend fun clear()
}