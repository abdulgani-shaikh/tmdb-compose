package com.shaikhabdulgani.tmdb.di

import com.shaikhabdulgani.tmdb.auth.data.repository.FirebaseAuthRepository
import com.shaikhabdulgani.tmdb.auth.domain.repository.AuthRepository
import com.shaikhabdulgani.tmdb.core.data.repository.UserRepositoryImpl
import com.shaikhabdulgani.tmdb.core.domain.repository.UserRepository
import com.shaikhabdulgani.tmdb.home.data.repository.HomeRepositoryImpl
import com.shaikhabdulgani.tmdb.home.domain.repository.HomeRepository
import com.shaikhabdulgani.tmdb.moviedetail.data.repository.MovieDetailRepositoryImpl
import com.shaikhabdulgani.tmdb.moviedetail.domain.repository.MovieDetailRepository
import com.shaikhabdulgani.tmdb.search.data.repository.SearchRepositoryImpl
import com.shaikhabdulgani.tmdb.search.domain.repository.SearchRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { FirebaseAuthRepository(get(), get()) } bind AuthRepository::class
    single<HomeRepository> { HomeRepositoryImpl(get(), get(), get()) } bind HomeRepository::class
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) } bind UserRepository::class
    single<SearchRepository> { SearchRepositoryImpl(get()) } bind SearchRepository::class
    single<MovieDetailRepository> { MovieDetailRepositoryImpl(get(), get()) } bind MovieDetailRepository::class
}