package com.shaikhabdulgani.tmdb.auth.presentation.signup

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

class SignUpViewModel (
    private val authRepository: AuthRepository,
    private val validator: AuthValidators
) : ViewModel() {

    var emailState by mutableStateOf(InputTextState())
        private set
    var passwordState by mutableStateOf(InputTextState())
        private set
    var rePasswordState by mutableStateOf(InputTextState())
        private set
    var usernameState by mutableStateOf(InputTextState())
        private set

    private val _authState = MutableSharedFlow<Resource<User>>()
    val authState: SharedFlow<Resource<User>> get() = _authState

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.SignUpClick -> signUp()
            is SignUpEvent.OnEmailChange -> onEmailChange(event.email)
            is SignUpEvent.OnPasswordChange -> onPasswordChange(event.password)
            is SignUpEvent.OnRePasswordChange -> onRePasswordChange(event.password)
            is SignUpEvent.OnUsernameChange -> onUsernameChange(event.username)
        }
    }

    private fun onRePasswordChange(password: String) {
        val validationResult: ValidationResult =
            validator.rePasswordValidator.validate(Pair(password, passwordState.value))
        if (validationResult.isValid) {
            rePasswordState = rePasswordState.noError(password)
            return
        }
        rePasswordState = InputTextState(
            value = password,
            error = validationResult.error
        )
    }

    private fun onUsernameChange(username: String) {
        val validationResult: ValidationResult = validator.usernameValidator.validate(username)
        if (validationResult.isValid) {
            usernameState = usernameState.noError(username)
            return
        }
        usernameState = InputTextState(
            value = username,
            error = validationResult.error
        )
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

    private fun signUp() = viewModelScope.launch(Dispatchers.IO) {
        validateEveryField()
        if (!isEveryFieldValid()) {
            return@launch
        }
        authRepository.signUp(
            email = emailState.value,
            username = usernameState.value,
            password = passwordState.value
        ).collect {
            _authState.emit(it)
        }
    }

    private fun validateEveryField() {
        onEmailChange(emailState.value)
        onPasswordChange(passwordState.value)
        onRePasswordChange(rePasswordState.value)
        onUsernameChange(usernameState.value)
    }

    private fun isEveryFieldValid(): Boolean {
        return emailState.value.isNotBlank() ||
                passwordState.value.isNotBlank() ||
//                rePasswordState.value.isNotBlank() ||
                usernameState.value.isNotBlank()
    }
}

