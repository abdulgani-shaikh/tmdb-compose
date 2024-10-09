package com.shaikhabdulgani.tmdb.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shaikhabdulgani.tmdb.BuildConfig
import com.shaikhabdulgani.tmdb.home.data.remote.HomeApi
import com.shaikhabdulgani.tmdb.home.data.remote.HomeApiImpl
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.MovieDetailApi
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.MovieDetailApiImpl
import com.shaikhabdulgani.tmdb.search.data.remote.SearchApi
import com.shaikhabdulgani.tmdb.search.data.remote.SearchApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single<SearchApi> { SearchApiImpl(get()) }
    single<MovieDetailApi> { MovieDetailApiImpl(get()) }
    single<HomeApi> { HomeApiImpl(get()) }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
}

private fun createHttpClient() = HttpClient(Android) {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(
            json = Json {
                ignoreUnknownKeys = true
            }
        )
    }
    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(BuildConfig.API_KEY, "")
            }
        }
    }
}