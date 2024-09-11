package com.shaikhabdulgani.tmdb.moviedetail.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaikhabdulgani.tmdb.home.domain.model.Media
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.CastEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.GenreEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.MovieDetailEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.local.entity.RecommendationMovieEntity
import com.shaikhabdulgani.tmdb.moviedetail.data.source.remote.dto.MovieDetailDto
import com.shaikhabdulgani.tmdb.moviedetail.domain.model.MovieDetail
import com.shaikhabdulgani.tmdb.search.domain.model.ContentType
import com.shaikhabdulgani.tmdb.search.domain.model.MediaType


fun MovieDetailDto.toMovieDetailEntity(type: String): MovieDetailEntity {
    return MovieDetailEntity(
        id = id,
        title = title,
        type = type,
        backdropPath = backdropPath,
        posterPath = posterPath,
        description = overview,
        runtime = runtime,
        releaseDate = releaseDate,
        rating = voteAverage,
        genres = genres.map { it.toGenreEntity().fromGenreEntity() }.fromList(),
        cast = credits.cast.map { it.toCastEntity().fromCastEntity() }.fromList(),
        recommendations = recommendations.results.map { it.toRecommendationMovieEntity().fromRecommendationMovieEntity() }.fromList(),
    )
}

fun MovieDetailEntity.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        backdropPath = backdropPath,
        posterPath = posterPath,
        title = title,
        type = ContentType.parse(type),
        description = description,
        runtime = runtime.toString(),
        releaseYear = releaseDate,
        rating = rating,
        genres = genres.toList().map { it.toGenreEntity().toGenre() },
        cast = cast.toList().map { it.toCastEntity().toCast() },
        recommendations = recommendations.toList().map { it.toRecommendationMovieEntity().toMovie() }
    )
}

fun MovieDetailDto.toMedia(type: MediaType): Media{
    return Media(
        id = id,
        title = title,
        imageId = posterPath,
        mediaType = type
    )
}

//@TypeConverter
fun GenreEntity.fromGenreEntity(): String {
    return Gson().toJson(this)
}

//@TypeConverter
fun String.toGenreEntity(): GenreEntity {
    return Gson().fromJson(this, GenreEntity::class.java)
}

//@TypeConverter
fun CastEntity.fromCastEntity(): String {
    return Gson().toJson(this)
}

//@TypeConverter
fun String.toCastEntity(): CastEntity {
    return Gson().fromJson(this, CastEntity::class.java)
}

//@TypeConverter
fun RecommendationMovieEntity.fromRecommendationMovieEntity(): String {
    return Gson().toJson(this)
}

//@TypeConverter
fun String.toRecommendationMovieEntity(): RecommendationMovieEntity {
    return Gson().fromJson(this, RecommendationMovieEntity::class.java)
}
//@TypeConverters
fun String.toList(): List<String> {
    return Gson().fromJson(this, object : TypeToken<List<String>>() {}.type)
}

//@TypeConverters
fun List<String>.fromList(): String {
    return Gson().toJson(this.toTypedArray())
}