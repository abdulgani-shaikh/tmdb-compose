package com.shaikhabdulgani.tmdb.profile.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.base.SessionViewModel
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import com.shaikhabdulgani.tmdb.core.presentation.util.emptyUser
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : SessionViewModel(authRepository) {

    companion object{
        private const val TAG = "ProfileViewModel"
    }

    var user by mutableStateOf(emptyUser())
        private set

    var favorites by mutableStateOf<List<Media>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    fun getProfileData() = viewModelScope.launch(Dispatchers.IO) {
        if (uid.isBlank()){
            Log.w(TAG,"uid is empty")
            return@launch
        }
        when(val userResult = userRepository.getUser(uid,true)){
            is Result.Failure -> {}
            is Result.Success -> {
                user = userResult.data!!
            }
        }
        userRepository.getFavorites(uid).collect{
            when(it){
                is Resource.Error -> isLoading = true
                is Resource.Loading -> {
                    isLoading = false
                    Log.e(TAG,it.message ?: "error fetching favorites")
                }
                is Resource.Success -> {
                    isLoading = false
                    favorites = it.data!!
                }
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        authRepository.logout()
        userRepository.clear()
    }
}