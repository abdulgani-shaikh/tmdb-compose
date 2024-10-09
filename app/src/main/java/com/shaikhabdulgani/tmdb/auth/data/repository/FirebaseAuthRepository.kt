package com.shaikhabdulgani.tmdb.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.base.BaseRepository
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.data.util.await
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : AuthRepository, BaseRepository() {
    override suspend fun login(email: String, password: String): Flow<Resource<User>> {
        return executeWithFlow {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            if (!authResult.isSuccessful || authResult.result == null || authResult.result.user == null) {
                //unsuccessful case
                if (authResult.exception != null && authResult.exception!!.message != null) {
                    throw Exception(authResult.exception!!.message!!)
                } else {
                    // unknown unsuccessful case
                    throw Exception("Some error occurred")
                }
            }

            val user =
                when (val userResult = userRepository.getUser(authResult.result.user!!.uid, true)) {
                    is Result.Failure -> {
                        throw Exception(userResult.error)
                    }

                    is Result.Success -> {
                        userResult.data!!
                    }
                }
            return@executeWithFlow user
        }
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<User>> {
        return executeWithFlow {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            if (!authResult.isSuccessful || authResult.result == null || authResult.result.user == null) {
                //unsuccessful case
                if (authResult.exception != null && authResult.exception!!.message != null) {
                    throw Exception(authResult.exception!!.message!!)
                } else {
                    // unknown unsuccessful case
                    throw Exception("Some error occurred")
                }
            }

            val userResult = userRepository.addUser(
                uid = authResult.result.user!!.uid,
                username = username,
                email = email
            )
            val user = when (userResult) {
                is Result.Failure -> {
                    throw Exception(userResult.error)
                }

                is Result.Success -> {
                    userResult.data!!
                }
            }
            return@executeWithFlow user
        }
    }

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun uuid() = auth.currentUser?.uid ?: ""

    override suspend fun getLoggedInUser() = userRepository.getUser(uuid()).data

    override suspend fun logout() = auth.signOut()
}