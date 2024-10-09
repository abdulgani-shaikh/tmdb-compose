package com.shaikhabdulgani.tmdb.di

import com.shaikhabdulgani.tmdb.auth.domain.validation.AuthValidators
import com.shaikhabdulgani.tmdb.auth.domain.validation.EmailValidator
import com.shaikhabdulgani.tmdb.auth.domain.validation.PasswordValidator
import com.shaikhabdulgani.tmdb.auth.domain.validation.RepeatPasswordValidator
import com.shaikhabdulgani.tmdb.auth.domain.validation.UsernameValidator
import com.shaikhabdulgani.tmdb.auth.presentation.login.LoginScreen
import com.shaikhabdulgani.tmdb.auth.presentation.login.LoginViewModel
import com.shaikhabdulgani.tmdb.auth.presentation.signup.SignUpViewModel
import com.shaikhabdulgani.tmdb.home.presentation.HomeViewModel
import com.shaikhabdulgani.tmdb.moviedetail.presentation.MovieDetailViewModel
import com.shaikhabdulgani.tmdb.profile.presentation.ProfileViewModel
import com.shaikhabdulgani.tmdb.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthValidators> {
        AuthValidators(
            emailValidator = EmailValidator(),
            passwordValidator = PasswordValidator(),
            rePasswordValidator = RepeatPasswordValidator(),
            usernameValidator = UsernameValidator(),
        )
    }

    viewModel { HomeViewModel(get(),get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { SignUpViewModel(get(),get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { MovieDetailViewModel(get(),get(),get()) }
    viewModel { ProfileViewModel(get(),get()) }
}