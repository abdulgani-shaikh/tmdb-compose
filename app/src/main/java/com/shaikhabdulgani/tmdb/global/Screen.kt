package com.shaikhabdulgani.tmdb.global

import com.shaikhabdulgani.tmdb.search.domain.model.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object SignUp : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    class MovieDetail(val id: Int, val mediaType: String = MediaType.MOVIE.getValue()) : Screen
}