package com.shaikhabdulgani.tmdb.moviedetail.data.source.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.CastEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.GenreEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.RecommendationMovieEntity


class MovieDetailConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromGenreEntity(genreEntity: GenreEntity): String {
        return gson.toJson(genreEntity)
    }

    @TypeConverter
    fun toGenreEntity(string: String): GenreEntity {
        return gson.fromJson(string, GenreEntity::class.java)
    }

    @TypeConverter
    fun fromCastEntity(castEntity: CastEntity): String {
        return gson.toJson(castEntity)
    }

    @TypeConverter
    fun toCastEntity(string: String): CastEntity {
        return gson.fromJson(string, CastEntity::class.java)
    }

    @TypeConverter
    fun fromRecommendationMovieEntity(movieEntity: RecommendationMovieEntity): String {
        return gson.toJson(movieEntity)
    }

    @TypeConverter
    fun toRecommendationMovieEntity(string: String): RecommendationMovieEntity {
        return gson.fromJson(string, RecommendationMovieEntity::class.java)
    }

//    @TypeConverters
//    fun toList(string: String): List<RecommendationMovieEntity> {
//        return gson.fromJson(string, object : TypeToken<List<RecommendationMovieEntity>>() {}.type)
//    }
//
//    @TypeConverters
//    fun fromList(list: List<RecommendationMovieEntity>): String {
//        return gson.toJson(list.toTypedArray())
//    }
//
//    @TypeConverters
//    fun toGenreEntityList(string: String): List<GenreEntity> {
//        return gson.fromJson(string, object : TypeToken<List<GenreEntity>>() {}.type)
//    }
//
//    @TypeConverters
//    fun fromGenreEntityList(list: List<GenreEntity>): String {
//        return gson.toJson(list.toTypedArray())
//    }
//
//    @TypeConverters
//    fun toCastEntityList(string: String): List<CastEntity> {
//        return gson.fromJson(string, object : TypeToken<List<CastEntity>>() {}.type)
//    }
//
//    @TypeConverters
//    fun fromCastEntityList(list: List<CastEntity>): String {
//        return gson.toJson(list.toTypedArray())
//    }

    @TypeConverters
    fun toList(string: String): List<String> {
        return gson.fromJson(string, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverters
    fun fromList(list: List<String>): String {
        return gson.toJson(list.toTypedArray())
    }
}