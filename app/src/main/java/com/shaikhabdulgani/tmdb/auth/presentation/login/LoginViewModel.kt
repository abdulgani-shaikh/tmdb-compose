package com.shaikhabdulgani.tmdb.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.auth.domain.validation.AuthValidators
import com.shaikhabdulgani.tmdb.auth.domain.validation.ValidationResult
import com.shaikhabdulgani.tmdb.auth.presentation.InputTextState
import com.shaikhabdulgani.tmdb.core.domain.model.User
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validator: AuthValidators,
) : ViewModel() {

    var emailState by mutableStateOf(InputTextState())
        private set
    var passwordState by mutableStateOf(InputTextState())
        private set

    private val _authState = MutableSharedFlow<Resource<User>>()
    val authState: SharedFlow<Resource<User>> get() = _authState

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            LoginUiEvent.LoginClick -> login()
            is LoginUiEvent.OnEmailChange -> onEmailChange(event.email)
            is LoginUiEvent.OnPasswordChange -> onPasswordChange(event.password)
        }
    }

    private fun onPasswordChange(password: String) {
        val validationResult: ValidationResult = validator.passwordValidator.validate(password)
        if (validationResult.isValid) {
            passwordState = passwordState.noError(password)
            return
        }
        passwordState = InputTextState(
            value = password,
            error = validationResult.error
        )
    }

    private fun onEmailChange(email: String) {
        val validationResult: ValidationResult = validator.emailValidator.validate(email)
        if (validationResult.isValid) {
            emailState = emailState.noError(email)
            return
        }
        emailState = InputTextState(
            value = email,
            error = validationResult.error
        )
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {
        onEmailChange(emailState.value)
        onPasswordChange(passwordState.value)
        if (emailState.error.isNotBlank() || passwordState.error.isNotBlank()) {
            return@launch
        }

        authRepository.login(
            email = emailState.value,
            password = passwordState.value
        ).collect {
            _authState.emit(it)
        }
    }
}

