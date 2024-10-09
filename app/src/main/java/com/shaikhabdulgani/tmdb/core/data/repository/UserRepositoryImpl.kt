package com.shaikhabdulgani.tmdb.core.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.shaikhabdulgani.tmdb.base.BaseRepository
import com.shaikhabdulgani.tmdb.core.data.mapper.mapToUserDto
import com.shaikhabdulgani.tmdb.core.data.mapper.toUser
import com.shaikhabdulgani.tmdb.core.data.mapper.toUserEntity
import com.shaikhabdulgani.tmdb.core.data.source.local.AppDatabase
import com.shaikhabdulgani.tmdb.core.data.source.remote.FirebaseConstants
import com.shaikhabdulgani.tmdb.core.data.source.remote.dto.UserDto
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.data.util.await
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import com.shaikhabdulgani.tmdb.moviedetail.data.mapper.toMedia
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.MovieDetailApi
import com.shaikhabdulgani.tmdb.search.domain.model.MediaType
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val db: AppDatabase,
    private val movieRepository: MovieDetailApi
) : UserRepository, BaseRepository() {
    override suspend fun getUser(uid: String, forceRemoteFetch: Boolean): Result<User> {
        return execute {
            if (!forceRemoteFetch) {
                val user = db.userDao.getUser(uid)
                if (user != null) {
                    return@execute user.toUser()
                }
            }

            val collection = firestore.collection(FirebaseConstants.USER_COLLECTION)
            val result = collection.document(uid).get().await()

            if (!result.isSuccessful) {
                result.exception?.printStackTrace()
                throw IllegalStateException(result.exception?.message ?: "error fetching user")
            }

            if(!result.result.exists()){
                throw IllegalArgumentException("user not found with id $uid")
            }

            val userEntity = result.result.mapToUserDto().toUserEntity()
            db.userDao.saveUser(userEntity)
            userEntity.toUser()
        }
    }

    override suspend fun addUser(uid: String, username: String, email: String): Result<User> {
        return execute {
            val collection = firestore.collection(FirebaseConstants.USER_COLLECTION)
            val user = UserDto(
                uid = uid,
                username = username,
                email = email,
                favorites = emptyList()
            )
            val result = collection.document(uid).set(user).await()
            if (!result.isSuccessful) {
                throw Exception(result.exception?.message ?: "error saving user")
            }
            val userEntity = user.toUserEntity()
            db.userDao.saveUser(userEntity)
            userEntity.toUser()
        }
    }

    override suspend fun bookmarkMovie(uid: String, movieId: String): Result<Boolean> {
        return execute {
            val result = firestore
                .collection(FirebaseConstants.USER_COLLECTION)
                .document(uid)
                .update(
                    FirebaseConstants.USER_FAVORITES,
                    FieldValue.arrayUnion(movieId)
                ).await()

            result.isSuccessful
        }
    }

    override suspend fun removeMovieBookmark(uid: String, movieId: String): Result<Boolean> {
        return execute {
            val result = firestore
                .collection(FirebaseConstants.USER_COLLECTION)
                .document(uid)
                .update(
                    FirebaseConstants.USER_FAVORITES,
                    FieldValue.arrayRemove(movieId)
                ).await()

            result.isSuccessful
        }
    }

    override suspend fun isBookmarked(uid: String, movieId: String): Result<Boolean> {
        return execute {
            val user = getUser(uid,true)
            if(user is Result.Success){
                user.data?.favorites?.contains(movieId) ?: false
            }else{
                false
            }
        }
    }

    override suspend fun getFavorites(
        uid: String,
        forceRemoteFetch: Boolean
    ): Flow<Resource<List<Media>>> {
        return executeWithFlow {
            val task = firestore.collection(FirebaseConstants.USER_COLLECTION)
                .document(uid)
                .get()
                .await()
            if (!task.isSuccessful || task.result == null) {
                throw Exception("cannot fetch user")
            }
            val movieIds = task.result.mapToUserDto().favorites.reversed()
            val result = mutableListOf<Media>()
            movieIds.forEach { id ->
                val movieDetail = movieRepository.getMovieDetail(id.toInt(), MediaType.MOVIE.getValue(), "")
                result.add(movieDetail.toMedia(MediaType.MOVIE))
            }
            result
        }
    }

    override suspend fun clear() {
        db.userDao.clear()
    }
}