package com.shaikhabdulgani.tmdb.core.presentation.dummy

import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import com.shaikhabdulgani.tmdb.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

object DummyHomeRepo : HomeRepository {
    override suspend fun getTrendingMovies(page: Int): Result<List<Media>> {
        return Result.failure()
    }

    override suspend fun getUpcomingMovies(page: Int): Result<List<Media>> {
        return Result.failure()
    }

    override suspend fun getPopularSeries(page: Int): Result<List<Media>> {
        return Result.failure()
    }

    override suspend fun getOnTheAirSeries(page: Int): Result<List<Media>> {
        return Result.failure()
    }
}

object DummyAuthRepo : AuthRepository{
    override suspend fun login(email: String, password: String): Flow<Resource<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<User>> {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun uuid(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getLoggedInUser(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}

object DummyUserRepo : UserRepository{
    override suspend fun getUser(uid: String, forceRemoteFetch: Boolean): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(uid: String, username: String, email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun bookmarkMovie(uid: String, movieId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeMovieBookmark(uid: String, movieId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun isBookmarked(uid: String, movieId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(
        uid: String,
        forceRemoteFetch: Boolean
    ): Flow<Resource<List<Media>>> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

}