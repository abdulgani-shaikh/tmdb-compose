package com.shaikhabdulgani.tmdb.moviedetail.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.moviedetail.domain.repository.MovieDetailRepository
import com.shaikhabdulgani.tmdb.moviedetail.presentation.util.emptyMovieDetail
import com.shaikhabdulgani.tmdb.search.domain.model.ContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var movieDetail by mutableStateOf(emptyMovieDetail())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isBookmarked by mutableStateOf(false)
        private set

    fun onEvent(event: DetailEvent){
        when(event){
            DetailEvent.BookmarkClick -> onBookmarkClick()
        }
    }

    fun getMovieDetails(id: Int, contentType: ContentType) = viewModelScope.launch(Dispatchers.IO) {
        isLoading = true
        val uid = authRepository.uuid()
        repository.getMovieDetail(id, contentType).collect {
            when (it) {
                is Resource.Error -> {
                    isLoading = false
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    isLoading = false
                    if (it.data != null) {
                        movieDetail = it.data
                    }
                }
            }
        }
        val result = userRepository.isBookmarked(uid, id.toString())
        result.data?.let {
            isBookmarked = it
        }
    }

    private fun onBookmarkClick() = viewModelScope.launch(Dispatchers.IO) {
        val userId = authRepository.uuid()
        val movieId = movieDetail.id.toString()
        if (isBookmarked) {
            isBookmarked = !isBookmarked
            removeBookmark(userId, movieId)
        } else {
            isBookmarked = !isBookmarked
            bookmarkMovie(userId, movieId)
        }
    }

    private suspend fun bookmarkMovie(userId: String, movieId: String) {
        val result = userRepository.bookmarkMovie(userId, movieId)
        if (result is Result.Success) {
            isBookmarked = true
        }
    }

    private suspend fun removeBookmark(userId: String, movieId: String) {
        val result = userRepository.removeMovieBookmark(userId, movieId)
        if (result is Result.Success) {
            isBookmarked = false
        }
    }

}

