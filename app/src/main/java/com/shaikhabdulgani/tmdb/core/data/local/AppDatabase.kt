package com.shaikhabdulgani.tmdb.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shaikhabdulgani.tmdb.home.data.local.dao.MovieDao
import com.shaikhabdulgani.tmdb.home.data.local.dao.SeriesDao
import com.shaikhabdulgani.tmdb.home.data.local.entity.MovieEntity
import com.shaikhabdulgani.tmdb.home.data.local.entity.SeriesEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.MovieDetailConverters
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.MovieDetailDao
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.MovieDetailEntity

@Database(
    entities = [
        MovieEntity::class,
        SeriesEntity::class,
        MovieDetailEntity::class
   ],
    version = 1
)
@TypeConverters(MovieDetailConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val seriesDto: SeriesDao
    abstract val movieDetailDao: MovieDetailDao
}