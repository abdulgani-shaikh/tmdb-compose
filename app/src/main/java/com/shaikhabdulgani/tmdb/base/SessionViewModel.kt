package com.shaikhabdulgani.tmdb.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import kotlinx.coroutines.launch

abstract class SessionViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var isLoggedIn by mutableStateOf(true)
        private set

    var uid by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            isLoggedIn = authRepository.isLoggedIn()
            if (isLoggedIn){
                uid = authRepository.uuid()
            }
        }
    }
}