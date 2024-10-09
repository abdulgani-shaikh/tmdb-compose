package com.shaikhabdulgani.tmdb.di

import androidx.room.Room
import com.shaikhabdulgani.tmdb.core.data.source.local.AppDatabase
import com.shaikhabdulgani.tmdb.core.data.source.local.UserDao
import com.shaikhabdulgani.tmdb.home.data.local.dao.MovieDao
import com.shaikhabdulgani.tmdb.home.data.local.dao.SeriesDao
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.MovieDetailDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single<MovieDao> { get<AppDatabase>().movieDao }
    single<SeriesDao> { get<AppDatabase>().seriesDao }
    single<MovieDetailDao> { get<AppDatabase>().movieDetailDao }
    single<UserDao> { get<AppDatabase>().userDao }
}